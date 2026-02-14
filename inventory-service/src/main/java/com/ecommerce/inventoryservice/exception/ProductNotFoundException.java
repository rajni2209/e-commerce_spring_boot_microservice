package com.ecommerce.inventoryservice.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(){
        super("product not found");
    }

}
