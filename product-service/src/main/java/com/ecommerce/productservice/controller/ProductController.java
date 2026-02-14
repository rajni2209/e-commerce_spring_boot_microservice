package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.DTO.ProductRequest;
import com.ecommerce.productservice.DTO.ProductResponse;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getAllProduct(@PathVariable Long id){
        Product byId = productService.getById(id);
        return ResponseEntity.ok(ProductResponse.from(byId));

    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> activeProduct(){
        return ResponseEntity.ok(productService.listAvailableProduct());
    }

    @PostMapping()
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request){
        return ResponseEntity.ok(ProductResponse.from(productService.create(request)));
    }

}
