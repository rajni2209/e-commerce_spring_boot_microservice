package com.ecommerce.orderservice.kafka;

import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.events.*;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.InboxServices;
import com.ecommerce.orderservice.service.OrderService;
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
public class PaymentEventListener {

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final OrderEventProducer eventProducer;
    private final InboxServices inboxService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "payment-events",
            groupId = "order-service-group",
            containerFactory = "eventEnvelopeKafkaListenerContainerFactory"
    )
    @Transactional
    public void onPaymentCompleted(String message) throws Exception {

        EventEnvelope<PaymentCompletedEvent> envelope =
                objectMapper.readValue(
                        message,
                        objectMapper.getTypeFactory()
                                .constructParametricType(
                                        EventEnvelope.class,
                                        PaymentCompletedEvent.class
                                )
                );

        boolean isNew = inboxService.markReceivedIfNew(
                envelope.getEventId(),
                envelope.getEventType()
        );
        if (!isNew) return;

        PaymentCompletedEvent event = envelope.getPayload();

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow();

        order.setPaymentCompleted(true);
        order.setPaymentStatus(event.getStatus());
        orderRepository.save(order);

        Order fresh = orderRepository.findById(order.getId()).orElseThrow();


        if ("FAILED".equals(event.getStatus())) {
            order.setStatus("CANCELLED");
            orderRepository.save(order);

            eventProducer.onOrderCancelled(
                    new OrderCancelledEvent(order.getId(), "Payment failed")
            );
            return;
        }

        orderService.evaluateOrder(order);
    }

}
