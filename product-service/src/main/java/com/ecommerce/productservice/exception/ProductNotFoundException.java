package com.ecommerce.productservice.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException() {
        super("product not found");
    }
}
