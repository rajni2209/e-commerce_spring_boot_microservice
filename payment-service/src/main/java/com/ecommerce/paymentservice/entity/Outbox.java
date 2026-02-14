package com.ecommerce.paymentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "outbox_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Outbox {

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
