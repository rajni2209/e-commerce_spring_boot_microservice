package com.ecommerce.paymentservice.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
public class PaymentRequest {

    @NotNull
    private Long orderId;

    @Positive
    private BigDecimal amount;

}
