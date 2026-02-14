package com.ecommerce.inventoryservice.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;

}
