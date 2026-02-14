package com.ecommerce.orderservice.kafka;

import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.events.EventEnvelope;
import com.ecommerce.orderservice.events.InventoryFailedEvent;
import com.ecommerce.orderservice.events.OrderCancelledEvent;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.InboxServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryFailedHandler {

    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;

    private final ObjectMapper objectMapper;
    private final InboxServices inboxService;

    @KafkaListener(
            topics = "inventory-failed-events",
            groupId = "order-service-group"
    )
    @Transactional
    public void onInventoryFailed(String message) throws Exception {

        EventEnvelope<InventoryFailedEvent> envelope =
                objectMapper.readValue(
                        message,
                        objectMapper.getTypeFactory()
                                .constructParametricType(EventEnvelope.class, InventoryFailedEvent.class)
                );

        boolean isNew = inboxService.markReceivedIfNew(envelope.getEventId(), envelope.getEventType());
        if (!isNew) {
            log.info("Duplicate ignored eventId={}", envelope.getEventId());
            return;
        }

        InventoryFailedEvent event = envelope.getPayload();

        log.info("event=InventoryFailedEvent received orderId={}",event.getOrderId());
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("order not found"));

        if ("CANCELLED".equals(order.getStatus())){
            log.info("order already cancelled, skipping orderId={}", order.getId());
            return;
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
        eventProducer.onOrderCancelled(
                new OrderCancelledEvent(
                        order.getId(),
                        event.getReason()
                )
        );
        log.warn("event=OrderCancelledEvent produced orderId={}",event.getOrderId());
    }
}
