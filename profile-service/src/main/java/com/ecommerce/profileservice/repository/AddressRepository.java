package com.ecommerce.profileservice.repository;

import com.ecommerce.profileservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address , Long> {
}
