package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.entity.InboxEvent;
import com.ecommerce.inventoryservice.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class InboxService {

    private final InboxRepository inboxRepository;

    public boolean markIfNew(String eventId , String eventType){
        if (inboxRepository.existsByEventId(eventId)){
            return false;
        }

        InboxEvent inboxEvent = InboxEvent.builder()
                .eventId(eventId)
                .eventType(eventType)
                .receivedAt(Instant.now())
                .status("RECEIVED")
                .build();
        inboxRepository.save(inboxEvent);
        return true;
    }

}
