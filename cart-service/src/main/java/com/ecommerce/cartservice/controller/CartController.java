package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.DTO.AddToCartRequest;
import com.ecommerce.cartservice.DTO.CartResponse;
import com.ecommerce.cartservice.model.Cart;
import com.ecommerce.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> fetchCart(
            @RequestHeader("X-User-Id") Long userId
    ) {
        log.info("GET /cart called userId={}", userId);
        Cart cart = cartService.getCart(userId);
        log.info("GET /cart success userId={} itemsCount={}", userId, cart.getItems().size());
        return ResponseEntity.ok(CartResponse.from(cart));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItems(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody AddToCartRequest request
    ) {
        log.info("POST /cart/items called userId={} productId={} qty={}",
                userId, request.getProductId(), request.getQuantity());
        Cart cart = cartService.addItem(userId, request);
        log.info("POST /cart/items success userId={} itemsCount={}", userId, cart.getItems().size());
        return ResponseEntity.ok(CartResponse.from(cart));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> deleteItems(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId
    ) {
        log.info("DELETE /cart/items/{} called userId={}", productId, userId);
        Cart cart = cartService.removeCart(userId, productId);
        log.info("DELETE /cart/items/{} success userId={} itemsCount={}", productId, userId, cart.getItems().size());
        return ResponseEntity.ok(CartResponse.from(cart));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCart(
            @RequestHeader("X-User-Id") Long userId
    ) {
        log.info("DELETE /cart called userId={}", userId);
        cartService.clearCart(userId);
        log.info("DELETE /cart success userId={}", userId);
        return ResponseEntity.ok("Cart cleared");
    }

}
