package com.ecommerce.orderservice.controller;


import com.ecommerce.orderservice.DTO.OrderResponse;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> order(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("Idempotency-Key") String key
    ){
        log.info("POST /order called userId={}",userId);
        Order order = orderService.createOrder(userId , key);
        log.info("POST /order success orderId={} userId={}" , order.getId(),userId);
        return ResponseEntity.ok(OrderResponse.from(order));
    }

}
