package com.ecommerce.authservice.exception;

import com.ecommerce.authservice.DTO.ErrorrResponce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourcesFoundException.class)
    public ResponseEntity<ErrorrResponce> runtimeException(Exception exception){
        ErrorrResponce errorrResponce = new ErrorrResponce();
        errorrResponce.setMessage(exception.getMessage());
        errorrResponce.setStatus(HttpStatus.FOUND.value());
        errorrResponce.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(errorrResponce , HttpStatus.FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> validationError(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
