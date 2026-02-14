package com.ecommerce.orderservice.kafka;

import com.ecommerce.orderservice.events.EventEnvelope;
import com.ecommerce.orderservice.events.OrderCancelledEvent;
import com.ecommerce.orderservice.events.OrderConfirmedEvent;
import com.ecommerce.orderservice.events.OrderCreatedEvent;
import com.ecommerce.orderservice.service.OutboxService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderEventProducer {

    private final OutboxService outboxService;
    private static final String TOPIC = "order-created";
    private static final String TOPIC2 = "order-confirmed-events";
    private static final String TOPIC3 = "order-cancelled-events";

    public void orderCreated(OrderCreatedEvent event){
        EventEnvelope<OrderCreatedEvent> envelope = new EventEnvelope<>(
                UUID.randomUUID().toString(),
                "OrderCreatedEvent",
                event.getOrderId(),
                Instant.now(),
                1,
                event
        );
        outboxService.saveData(TOPIC,envelope);
    }

    public void onOrderConfirmed(OrderConfirmedEvent event){
        EventEnvelope<OrderConfirmedEvent> envelope = new EventEnvelope<>(
                UUID.randomUUID().toString(),
                "OrderConfirmedEvent",
                event.getOrderId(),
                Instant.now(),
                1,
                event
        );
        outboxService.saveData(TOPIC2,envelope);
    }

    public void onOrderCancelled(OrderCancelledEvent event){
        EventEnvelope<OrderCancelledEvent> envelope = new EventEnvelope<>(
                UUID.randomUUID().toString(),
                "OrderCancelledEvent",
                event.getOrderId(),
                Instant.now(),
                1,
                event
        );
        outboxService.saveData(TOPIC3,envelope);
    }

}
