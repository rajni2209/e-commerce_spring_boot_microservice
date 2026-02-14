package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.DTO.CartResponse;;
import com.ecommerce.orderservice.DTO.OrderResponse;
import com.ecommerce.orderservice.client.CartClient;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderIdempotencyKey;
import com.ecommerce.orderservice.entity.OrderItem;
import com.ecommerce.orderservice.events.OrderConfirmedEvent;
import com.ecommerce.orderservice.events.OrderCreatedEvent;
import com.ecommerce.orderservice.events.OrderItemEvent;
import com.ecommerce.orderservice.exception.OrderCreationException;
import com.ecommerce.orderservice.kafka.OrderEventProducer;
import com.ecommerce.orderservice.repository.IdempotencyRepository;
import com.ecommerce.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final CartClient cartClient;
    private final OrderRepository orderRepository;
    private final IdempotencyRepository idempotencyRepository;
    private final OrderEventProducer eventProducer;
    private final OrderMetrics orderMetrics;

    @Transactional
    public Order createOrder(Long userId , String idempotencyKey){

        Optional<OrderIdempotencyKey> existing = idempotencyRepository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()){
            return orderRepository.findById(existing.get().getOrderId())
                    .orElseThrow();
        }


        CartResponse cart = cartClient.getCart(userId);
        log.info("event=CART_FETCHED_SUCCESSFULLY userId={}" , userId);

        if (cart.getItems().isEmpty()){
            log.warn("event=CART_EMPTY userId={}",userId);
            throw new OrderCreationException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("CREATED");

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItemEvent> itemEvents = new ArrayList<>();

        for (var item : cart.getItems()){

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setPrice(item.getPrice());
            orderItem.setProductName(item.getProductName());
            orderItem.setQuantity(item.getQuantity());

            order.getItems().add(orderItem);
            total = total.add(
                    item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
            itemEvents.add(
                    new OrderItemEvent(
                            item.getProductId(),
                            item.getQuantity()
                    )
            );
        }
        order.setTotalAmount(total);
        Order save = orderRepository.save(order);

        eventProducer.orderCreated(
                new OrderCreatedEvent(
                        save.getId(),
                        userId,
                        total,
                        itemEvents
                )
        );
        orderMetrics.incrementCreated();
        log.info("event=OrderCreatedEvent_PRODUCED orderId={} , userId={}",save.getId() , userId);
        return save;
    }

    @Transactional
    public void evaluateOrder(Order order){

        log.info("event=EVALUATING_ORDER_STARTED orderId={}",order.getId());
        if (order.isInventoryReserved() && order.isPaymentCompleted() && "SUCCESS".equals(order.getPaymentStatus())){
            order.setStatus("CONFIRMED");
            orderRepository.save(order);

            eventProducer.onOrderConfirmed(
                    new OrderConfirmedEvent(
                            order.getId(),
                            order.getUserId()
                    )
            );
            orderMetrics.incrementConfirmed();
            log.info("event=OrderConfirmedEvent produced orderId={}",order.getId());
        }
    }
}
