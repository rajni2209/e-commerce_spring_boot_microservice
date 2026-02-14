package com.ecommerce.paymentservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {

    private Long paymentId;
    private String status;

}
