package com.ecommerce.profileservice.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileRequest {

    @NotBlank()
    private String name;
    @NotBlank
    @Size(min = 10 , max = 13)
    private String phoneNumber;

}
