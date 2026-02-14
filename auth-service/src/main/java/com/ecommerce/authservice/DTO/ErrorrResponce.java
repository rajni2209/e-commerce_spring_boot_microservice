package com.ecommerce.authservice.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorrResponce {

    private int status;
    private String message;
    private LocalDateTime timestamp;

}
