package com.ecommerce.orderservice.exception;

import com.ecommerce.orderservice.DTO.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderCreationException.class)
    public ResponseEntity<ErrorResponse> unsufficientException(Exception exception){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setStatus(HttpStatus.NO_CONTENT.value());
        errorResponse.setTimestamp(LocalDateTime.now());
        log.error("event=OrderCreationException: {}" , exception.getMessage());
        return new ResponseEntity<>(errorResponse , HttpStatus.NO_CONTENT);
    }

}
