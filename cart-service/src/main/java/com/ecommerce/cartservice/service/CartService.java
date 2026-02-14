package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.DTO.AddToCartRequest;
import com.ecommerce.cartservice.DTO.ProductResponse;
import com.ecommerce.cartservice.feign.ProductClient;
import com.ecommerce.cartservice.model.Cart;
import com.ecommerce.cartservice.model.CartItems;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final RedisTemplate<String , Cart> redisTemplate;
    private final ProductServiceCaller productServiceCaller;

    private String key(Long userId){
        return "cart:"+userId;
    }

    public Cart getCart(Long userId){
        Cart cart = redisTemplate.opsForValue().get(key(userId));
        if (cart == null){
            log.info("Cart not found in redis, creating new cart userId={}", userId);
            return new Cart(userId , new ArrayList<>());
        }
        log.info("Cart found userId={} itemsCount={}", userId, cart.getItems().size());
        return cart;
    }

    public Cart addItem(Long userId , AddToCartRequest request){
        log.info("Add to cart request userId={} productId={} qty={}",
                userId, request.getProductId(), request.getQuantity());
        ProductResponse product = productServiceCaller.getProduct(request.getProductId());
        Cart cart = getCart(userId);
        cart.getItems().stream()
                .filter(i->i.getProductId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            item.setQuality(item.getQuality() + request.getQuantity());
                            log.info("Cart item updated userId={} productId={} newQty={}",
                                    userId, product.getId(), item.getQuality());
                        },
                        () ->{
                            cart.getItems().add(
                                    new CartItems(
                                            product.getId(),
                                            product.getName(),
                                            product.getPrice(),
                                            request.getQuantity()
                                    )
                            );
                            log.info("Cart item added userId={} productId={} qty={}",
                                    userId, product.getId(), request.getQuantity());
                        }
                );
        redisTemplate.opsForValue().set(key(userId) , cart);
        redisTemplate.expire(key(userId), Duration.ofMinutes(30));
        log.info("Cart saved in redis userId={} itemsCount={}", userId, cart.getItems().size());
        return cart;
    }

    public Cart removeCart(Long userId , Long productId){
        log.info("Remove from cart request userId={} productId={}", userId, productId);
        Cart cart = getCart(userId);
        boolean removed = cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        redisTemplate.opsForValue().set(key(userId), cart);
        if (removed) {
            log.info("Product removed from cart userId={} productId={}", userId, productId);
        } else {
            log.warn("Product not found in cart userId={} productId={}", userId, productId);
        }
        return cart;
    }

    public void clearCart(Long userId){
        log.info("Clear cart request userId={}", userId);
        redisTemplate.delete(key(userId));
        log.info("Cart cleared userId={}", userId);
    }

}
