package com.ecommerce.cartservice.feign;

import com.ecommerce.cartservice.DTO.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/product/{id}")
    ProductResponse getProduct(@PathVariable Long id);

}
