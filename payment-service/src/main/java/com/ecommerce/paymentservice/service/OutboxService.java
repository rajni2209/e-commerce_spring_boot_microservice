package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.entity.Outbox;
import com.ecommerce.paymentservice.event.EventEnvelope;
import com.ecommerce.paymentservice.repository.OutboxRepository;
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