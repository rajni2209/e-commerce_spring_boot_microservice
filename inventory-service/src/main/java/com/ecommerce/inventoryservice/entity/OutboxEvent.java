package com.ecommerce.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "inventory_outbox")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    private String eventId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private Long aggregatedId;

    @Lob
    @Column(nullable = false)
    private String payLoad;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private Instant sentAt;

    @Column(nullable = false)
    private Integer attempts;
}