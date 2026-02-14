package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.OrderIdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRepository extends JpaRepository<OrderIdempotencyKey , Long> {

    Optional<OrderIdempotencyKey> findByIdempotencyKey(String idempotencyKey);
    
}
