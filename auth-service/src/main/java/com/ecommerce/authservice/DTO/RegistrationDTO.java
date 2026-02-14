package com.ecommerce.authservice.DTO;

import com.ecommerce.authservice.entity.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDTO {

    @Email(message = "enter valid email id")
    @NotBlank(message = "enter email id")
    private String emailAddress;

    @NotBlank(message = "enter the password")
    @Size(min = 8, max = 15, message = "password length must be between 8 - 15")
    private String password;

    @NotNull(message = "please enter role")
    private RoleType role;
}
