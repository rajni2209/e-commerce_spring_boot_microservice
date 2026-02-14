package com.ecommerce.authservice.service;

import com.ecommerce.authservice.DTO.AuthResponce;
import com.ecommerce.authservice.DTO.LoginDTO;
import com.ecommerce.authservice.DTO.RegistrationDTO;
import com.ecommerce.authservice.entity.Role;
import com.ecommerce.authservice.entity.RoleType;
import com.ecommerce.authservice.entity.User;
import com.ecommerce.authservice.exception.ResourcesFoundException;
import com.ecommerce.authservice.repository.RoleRepository;
import com.ecommerce.authservice.repository.UserRepository;
import com.ecommerce.authservice.security.CustomUserDetails;
import com.ecommerce.authservice.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public User registration(RegistrationDTO registrationDTO) {
        log.info("Registration request received email={}", registrationDTO.getEmailAddress());
        if (userRepository.existsByEmail(registrationDTO.getEmailAddress())) {
            log.warn("Registration failed: user already exists email={}", registrationDTO.getEmailAddress());
            throw new ResourcesFoundException("User already exists");
        }
        RoleType roleType = registrationDTO.getRole();
        String roleName = "ROLE_" + roleType.name();

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->{
                    log.error("Registration failed: role not found roleName={}", roleName);
                    return  new RuntimeException("Role not found: " + roleName);
                });
        User user = modelMapper.map(registrationDTO, User.class);
        user.setEmail(registrationDTO.getEmailAddress());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        log.info("Registration successfully userId={} emailId={}",savedUser.getId() , savedUser.getEmail());
        return savedUser;
    }

    public AuthResponce login(LoginDTO request) {
        log.info("Login attempt email={}" , request.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            CustomUserDetails userDetails =
                    (CustomUserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);
            log.info("Login success email={}" , request.getEmail());
            return new AuthResponce(token);
        }catch (BadCredentialsException e){
            log.warn("Login failed:bad credentials email={}" , request.getEmail());
            throw e;
        }
    }
}

