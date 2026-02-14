package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.entity.Outbox;
import com.ecommerce.orderservice.events.EventEnvelope;
import com.ecommerce.orderservice.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

            Outbox outbox = Outbox.builder()
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
