package com.ecommerce.profileservice.DTO;

import com.ecommerce.profileservice.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressResponse {

    private Long id;
    private String addressType;
    private String houseNumber;
    private String city;
    private String state;
    private String zip;

    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getAddressType(),
                address.getHouseNumber(),
                address.getCity(),
                address.getState(),
                address.getZip()
        );
    }
}
