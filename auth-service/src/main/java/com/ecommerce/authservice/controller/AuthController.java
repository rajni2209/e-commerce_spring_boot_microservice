package com.ecommerce.authservice.controller;

import com.ecommerce.authservice.DTO.AuthResponce;
import com.ecommerce.authservice.DTO.LoginDTO;
import com.ecommerce.authservice.DTO.RegistrationDTO;
import com.ecommerce.authservice.entity.User;
import com.ecommerce.authservice.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid  @RequestBody RegistrationDTO registrationDTO){
        log.info("POST /register email={}",registrationDTO.getEmailAddress());
        User registration = registerService.registration(registrationDTO);
        log.info("Registration success email={}",registrationDTO.getEmailAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body("registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponce> signIn(@Valid @RequestBody LoginDTO loginDTO){
        log.info("POST /login request email={}",loginDTO.getEmail());
        AuthResponce login = registerService.login(loginDTO);
        log.info("login success email={}",loginDTO.getEmail());
        return ResponseEntity.ok(login);
    }

}
