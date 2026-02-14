package com.ecommerce.profileservice.exception;

public class ProfileNotFoundException extends RuntimeException{

    public ProfileNotFoundException() {
        super("profile not found");
    }
}
