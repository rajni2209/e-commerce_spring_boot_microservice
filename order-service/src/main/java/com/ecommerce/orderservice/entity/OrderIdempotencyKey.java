package com.ecommerce.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "order_idempotency_keys",
        uniqueConstraints = @UniqueConstraint(columnNames = "idempotencyKey")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderIdempotencyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String idempotencyKey;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Instant createdAt;


}
