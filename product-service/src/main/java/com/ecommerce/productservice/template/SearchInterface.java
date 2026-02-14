package com.ecommerce.productservice.template;

import com.ecommerce.productservice.DTO.ProductIndexRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "search-service")
public interface SearchInterface {

    @PostMapping("/internal/index")
    void index(@RequestBody ProductIndexRequest productRequest);

}
