package com.ecommerce.cartservice.kafka;

import com.ecommerce.cartservice.event.OrderConfirmedEvent;
import com.ecommerce.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final CartService cartService;

    @KafkaListener(
            topics = "order-confirmed-events",
            groupId = "cart-service-group"
    )
    public void onOrderConfirmed(OrderConfirmedEvent event) {
        log.info("event=OrderConfirmedEvent_RECEIVED orderId={} , userId={}",event.getOrderId(),event.getUserId());
        cartService.clearCart(event.getUserId());
        log.info("event=CART_CLEARED orderId={}",event.getOrderId());
    }
}
