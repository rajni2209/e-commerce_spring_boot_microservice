package com.ecommerce.paymentservice.kafka;

import com.ecommerce.paymentservice.entity.Payment;
import com.ecommerce.paymentservice.event.EventEnvelope;
import com.ecommerce.paymentservice.event.OrderCreatedEvent;
import com.ecommerce.paymentservice.service.InboxService;
import com.ecommerce.paymentservice.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final PaymentService paymentService;
    private final PaymentEventProducer eventProducer;

    private final ObjectMapper objectMapper;
    private final InboxService inboxService;

    @KafkaListener(
            topics = "order-created",
            groupId = "payment-service-group"
    )
    @Transactional
    public void onOrderCreated(String message) throws Exception{
        EventEnvelope<OrderCreatedEvent> env =
                objectMapper.readValue(
                        message,
                        objectMapper.getTypeFactory()
                                .constructParametricType(EventEnvelope.class, OrderCreatedEvent.class)
                );

        if (!inboxService.markReceivedIfNew(env.getEventId(), env.getEventType())) {
            log.info("Duplicate payment ignored {}", env.getEventId());
            return;
        }

        OrderCreatedEvent event = env.getPayload();

        log.info("event=OrderCreatedEvent received orderId={}",event.getOrderId());
        Payment payment = paymentService.paymentCreation(
                event.getOrderId(),
                event.getTotalAmount()
        );
        eventProducer.publishResult(
                payment.getOrderId(),
                payment.getStatus()
        );
        log.info("event=PaymentEvent_PRODUCED orderId={}",event.getOrderId());
    }
}
