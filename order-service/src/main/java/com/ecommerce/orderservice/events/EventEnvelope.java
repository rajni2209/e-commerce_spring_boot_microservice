package com.ecommerce.orderservice.events;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEnvelope<T> {

    private String eventId;
    private String eventType;
    private Long aggregatedId;
    private Instant occurTime;
    private Integer version;
    private T payload;

}
