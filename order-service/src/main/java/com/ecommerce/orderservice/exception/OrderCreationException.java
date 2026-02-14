package com.ecommerce.orderservice.exception;

public class OrderCreationException extends RuntimeException{

    public OrderCreationException(String message){
        super(message);
    }

}
