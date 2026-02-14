package com.ecommerce.paymentservice.kafka;

import com.ecommerce.paymentservice.event.EventEnvelope;
import com.ecommerce.paymentservice.event.PaymentCompletedEvent;
import com.ecommerce.paymentservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final OutboxService outboxService;

    private static final String TOPIC = "payment-events";

    public void publishResult(Long orderId , String status){
        EventEnvelope<PaymentCompletedEvent> envelope = new EventEnvelope<>(
                UUID.randomUUID().toString(),
                "PaymentCompletedEvent",
                orderId,
                Instant.now(),
                1,
                new PaymentCompletedEvent(orderId,status)
        );
        outboxService.saveData(TOPIC,envelope);
    }
}
