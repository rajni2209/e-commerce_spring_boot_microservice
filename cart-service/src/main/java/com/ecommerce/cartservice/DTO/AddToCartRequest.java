package com.ecommerce.cartservice.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddToCartRequest {

    @NotNull(message = "Product ID must not be null")
    @Positive(message = "Product ID must be positive")
    private Long productId;

    @Positive(message = "Quantity must be greater than zero")
    private int quantity;}
