package com.ecommerce.orderservice.client;

import com.ecommerce.orderservice.DTO.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart-service")
public interface CartClient {

    @GetMapping("/cart")
    public CartResponse getCart(
            @RequestHeader("X-User-Id") Long userId
    );

    @DeleteMapping("/cart")
    public void clearCart(
            @RequestHeader("X-User-Id") Long userId
    );

}
