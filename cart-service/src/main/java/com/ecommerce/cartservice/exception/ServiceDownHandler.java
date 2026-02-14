package com.ecommerce.cartservice.exception;

public class ServiceDownHandler extends RuntimeException{

    public ServiceDownHandler(String message){
        super(message);
    }

}
