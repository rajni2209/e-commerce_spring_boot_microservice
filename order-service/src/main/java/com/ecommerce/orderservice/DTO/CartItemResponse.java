package com.ecommerce.orderservice.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private Long productId;
    private String productName;
    @JsonProperty("productPrice")
    private BigDecimal price;
    @JsonProperty("quality")
    private int quantity;

}
