package com.ecommerce.profileservice.DTO;

import com.ecommerce.profileservice.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfileResponse {

    private String name;
    private String phoneNumber;
    private List<AddressResponse> addressResponces;

    public static ProfileResponse from(Profile profile) {
        return new ProfileResponse(
                profile.getName(),
                profile.getPhoneNumber(),
                profile.getAddresses()
                        .stream()
                        .map(AddressResponse::from)
                        .toList()
        );
    }

}
