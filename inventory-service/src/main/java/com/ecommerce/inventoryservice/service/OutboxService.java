package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.entity.OutboxEvent;
import com.ecommerce.inventoryservice.event.EventEnvelope;
import com.ecommerce.inventoryservice.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void saveData(String topic , EventEnvelope<?> eventEnvelope){
        try {
            String json = objectMapper.writeValueAsString(eventEnvelope);

            OutboxEvent outbox = OutboxEvent.builder()
                    .eventId(eventEnvelope.getEventId())
                    .topic(topic)
                    .eventType(eventEnvelope.getEventType())
                    .aggregatedId(eventEnvelope.getAggregatedId())
                    .payLoad(json)
                    .createdAt(Instant.now())
                    .status("NEW")
                    .attempts(0)
                    .build();
            outboxRepository.save(outbox);

        } catch (Exception e) {
            throw new RuntimeException("failed to serialize outbox event" ,e);
        }
    }

}
