package com.ecommerce.orderservice.kafka;

import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.events.EventEnvelope;
import com.ecommerce.orderservice.events.InventoryReservedEvent;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.InboxServices;
import com.ecommerce.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryReservedHandler {

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final InboxServices inboxServices;
    private final ObjectMapper objectMapper;


    @KafkaListener(
            topics = "inventory-reserved-events",
            groupId = "order-service-group",
            containerFactory = "eventEnvelopeKafkaListenerContainerFactory"
    )
    @Transactional
    public void onInventoryReserved(String message) throws Exception {

        EventEnvelope<InventoryReservedEvent> envelope =
                objectMapper.readValue(
                        message,
                        objectMapper.getTypeFactory()
                                .constructParametricType(
                                        EventEnvelope.class,
                                        InventoryReservedEvent.class
                                )
                );

        boolean isNew = inboxServices.markReceivedIfNew(
                envelope.getEventId(),
                envelope.getEventType()
        );
        if (!isNew) return;

        InventoryReservedEvent event = envelope.getPayload();

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow();

        if (order.isInventoryReserved()) return;

        order.setInventoryReserved(true);
        orderRepository.save(order);

        Order fresh = orderRepository.findById(order.getId()).orElseThrow();
        orderService.evaluateOrder(fresh);
    }

}
