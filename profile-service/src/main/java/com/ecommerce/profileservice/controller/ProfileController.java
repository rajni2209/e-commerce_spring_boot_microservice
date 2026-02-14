package com.ecommerce.profileservice.controller;

import com.ecommerce.profileservice.DTO.AddressRequest;
import com.ecommerce.profileservice.DTO.AddressResponse;
import com.ecommerce.profileservice.DTO.ProfileRequest;
import com.ecommerce.profileservice.DTO.ProfileResponse;
import com.ecommerce.profileservice.entity.Profile;
import com.ecommerce.profileservice.service.ProfileService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(
            @RequestHeader("X-User-Id") String userId) {

        Profile profile = profileService.getUserProfile(userId);

        if (profile == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ProfileResponse.from(profile));
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> update(
            @RequestHeader("X-User-Id") String userId,
            @Valid
            @RequestBody ProfileRequest profileRequest
            ){

        Profile createOrUpdate = profileService.createOrUpdate(userId, profileRequest);

        return ResponseEntity.ok(ProfileResponse.from(createOrUpdate));

    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressResponse>> getAddress(
            @RequestHeader("X-User-Id") String userId) {

        List<AddressResponse> addresses =
                profileService.getAddressesByUserId(userId);

        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/address")
    public ResponseEntity<ProfileResponse> addAddress(
            @RequestHeader("X-User-Id") String userId,
            @Valid
            @RequestBody AddressRequest request
            ){

        Profile profile = profileService.addAddress(userId, request);
        return ResponseEntity.ok(ProfileResponse.from(profile));

    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<ProfileResponse> updateAddress(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long addressId,
            @Valid
            @RequestBody AddressRequest request
    ){
        Profile profile = profileService.updateAddress(userId, addressId,request);
        return ResponseEntity.ok(ProfileResponse.from(profile));
    }

    @DeleteMapping("/address/{addressId}")
    private ResponseEntity<ProfileResponse> deleteAddress(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long addressId
    ){
        Profile profile = profileService.deleteAddress(userId ,addressId);
        return ResponseEntity.ok(ProfileResponse.from(profile));
    }


}
