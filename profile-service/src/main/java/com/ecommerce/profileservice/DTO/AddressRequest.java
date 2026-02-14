package com.ecommerce.profileservice.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {

    @NotBlank(message = "address type required")
    private String AddressType;

    @NotBlank(message = "house number required")
    private String houseNumber;

    @NotBlank(message = "state required")
    private String state;

    @NotBlank(message = "city required")
    private String city;

    @NotBlank(message = "zip required")
    private String zip;

}
