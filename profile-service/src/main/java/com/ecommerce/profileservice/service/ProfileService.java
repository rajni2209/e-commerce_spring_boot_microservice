package com.ecommerce.profileservice.service;

import com.ecommerce.profileservice.DTO.AddressRequest;
import com.ecommerce.profileservice.DTO.AddressResponse;
import com.ecommerce.profileservice.DTO.ProfileRequest;
import com.ecommerce.profileservice.entity.Address;
import com.ecommerce.profileservice.entity.Profile;
import com.ecommerce.profileservice.exception.ProfileNotFoundException;
import com.ecommerce.profileservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public Profile getUserProfile(String userId) {
        return profileRepository.findByUserID(userId).orElseThrow(
                ProfileNotFoundException::new
        );
    }

    public Profile createOrUpdate(String userId , ProfileRequest profileRequest){
        Profile profile = profileRepository.findByUserID(userId).orElse(
                new Profile()
        );

        profile.setUserID(userId);
        profile.setName(profileRequest.getName());
        profile.setPhoneNumber(profileRequest.getPhoneNumber());

        return profileRepository.save(profile);
    }

    public Profile addAddress(String userId , AddressRequest request){
        Profile profile = getUserProfile(userId);

        Address address = new Address();
        address.setAddressType(request.getAddressType());
        address.setHouseNumber(request.getHouseNumber());
        address.setState(request.getState());
        address.setCity(request.getCity());
        address.setZip(request.getZip());

        // owning side
        address.setProfile(profile);

        profile.getAddresses().add(address);

        return profileRepository.save(profile);

    }

    public Profile updateAddress(
            String userId,
            Long addressId,
            AddressRequest request) {

        Profile profile = getUserProfile(userId);

        Address address = profile.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Address not found"));

        address.setAddressType(request.getAddressType());
        address.setHouseNumber(request.getHouseNumber());
        address.setState(request.getState());
        address.setCity(request.getCity());
        address.setZip(request.getZip());

        return profileRepository.save(profile);
    }

    public Profile deleteAddress(String userId, Long addressId) {

        Profile profile = getUserProfile(userId);

        profile.getAddresses()
                .removeIf(a -> a.getId().equals(addressId));

        return profileRepository.save(profile);
    }

    public List<AddressResponse> getAddressesByUserId(String userId) {

        Profile profile = profileRepository.findByUserID(userId)
                .orElseThrow(ProfileNotFoundException::new);

        return profile.getAddresses()
                .stream()
                .map(AddressResponse::from)
                .toList();
    }

}
