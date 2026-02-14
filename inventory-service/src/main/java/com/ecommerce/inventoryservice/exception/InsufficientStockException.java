package com.ecommerce.inventoryservice.exception;

public class InsufficientStockException extends RuntimeException{

    public InsufficientStockException(){
        super("not sufficient quantity");
    }

}
