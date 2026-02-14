package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.entity.InboxEvent;
import com.ecommerce.paymentservice.repository.InboxEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@AllArgsConstructor
public class InboxService{

    private final InboxEventRepository eventRepository;

    @Transactional
    public boolean markReceivedIfNew(String eventId , String eventType){
        if (eventRepository.existsByEventId(eventId)){
            return false;
        }

        InboxEvent inboxEvent = InboxEvent.builder()
                .eventId(eventId)
                .eventType(eventType)
                .receivedAt(Instant.now())
                .status("RECEIVED")
                .build();
        eventRepository.save(inboxEvent);
        return true;
    }
}
