package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.DTO.ProductResponse;
import com.ecommerce.cartservice.exception.ServiceDownHandler;
import com.ecommerce.cartservice.feign.ProductClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceCaller {

    private final ProductClient productClient;

    @CircuitBreaker(name = "productService" , fallbackMethod = "fallbackGetProduct")
    @Retry(name = "productService")
    public ProductResponse getProduct(Long productId){
        return productClient.getProduct(productId);
    }

    public ProductResponse fallbackGetProduct(Long productId, Throwable throwable){
        log.error("Product service failed for productId={} reason={}", productId, throwable.toString());
        throw new ServiceDownHandler("Product service failed " + throwable.getMessage());
    }

}
