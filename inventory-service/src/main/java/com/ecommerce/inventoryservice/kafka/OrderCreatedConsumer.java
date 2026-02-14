package com.ecommerce.inventoryservice.kafka;

import com.ecommerce.inventoryservice.event.EventEnvelope;
import com.ecommerce.inventoryservice.event.OrderCreatedEvent;
import com.ecommerce.inventoryservice.service.InboxService;
import com.ecommerce.inventoryservice.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedConsumer {

    private final InventoryService inventoryService;
    private final InventoryEventProducer inventoryEventProducer;

    private final ObjectMapper objectMapper;
    private final InboxService inboxService;


    @KafkaListener(
            topics = "order-created",
            groupId =  "inventory-service-group"
    )
    @Transactional
    public void onOrderCreated(String message) throws Exception{

        EventEnvelope<OrderCreatedEvent> envelope =
                objectMapper.readValue(
                        message,
                        objectMapper.getTypeFactory()
                                .constructParametricType(EventEnvelope.class , OrderCreatedEvent.class)
                );

        boolean isNew = inboxService.markIfNew(envelope.getEventId(), envelope.getEventType());
        if (!isNew){
            log.info("Duplicate order-created ignored {}", envelope.getEventId());
            return;
        }

        OrderCreatedEvent event = envelope.getPayload();

        try {
            inventoryService.reserved(
                    event.getItems(),
                    event.getOrderId()
                        );
            inventoryEventProducer.publishReserved(event.getOrderId());
            log.info("event=InventoryReservedEvent_PUBLISHED orderId={}",event.getOrderId());
        }catch (Exception exception) {
            inventoryEventProducer.publishFailed(
                    event.getOrderId(),
                    exception.getMessage()
            );
            log.info("event=InventoryFailedEvent_PUBLISHED orderId={}",event.getOrderId());
        }
    }
}
