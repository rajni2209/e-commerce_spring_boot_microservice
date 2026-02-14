package com.ecommerce.profileservice.repository;

import com.ecommerce.profileservice.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile , Long> {

    Optional<Profile> findByUserID(String userID);

}
