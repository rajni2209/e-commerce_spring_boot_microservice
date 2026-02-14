package com.ecommerce.inventoryservice.kafka;

import com.ecommerce.inventoryservice.entity.OutboxEvent;
import com.ecommerce.inventoryservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String , String> kafkaTemplate;
    private static final int MAX_RETRIES = 5;
    private static final String DLQ_TOPIC = "inventory-outbox-dlq";


    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publish(){

        List<OutboxEvent> aNew = outboxRepository.findTop50ByStatusInOrderByCreatedAtAsc(List.of("NEW" , "FAILED"));
        for (OutboxEvent event : aNew){

            if (event.getAttempts() >= MAX_RETRIES){
                moveToDead(event);
                continue;
            }

            try {
                event.setStatus("IN_PROGRESS");
                outboxRepository.save(event);

                kafkaTemplate.send(
                        event.getTopic(),
                        String.valueOf(event.getAggregatedId()),
                        event.getPayLoad()
                ).get();
                event.setStatus("SENT");
                event.setSentAt(Instant.now());
                event.setAttempts(event.getAttempts()+1);
                outboxRepository.save(event);
            }catch (Exception e){
                event.setStatus("FAILED");
                event.setAttempts(event.getAttempts() + 1);
                log.error("Failed to publish event {}", event.getEventId(), e);
                outboxRepository.save(event);
            }
        }
    }


    public void moveToDead(OutboxEvent event){
        try {
            kafkaTemplate.send(
                    DLQ_TOPIC,
                    String.valueOf(event.getAggregatedId()),
                    event.getPayLoad()
            );
            event.setStatus("DEAD");
            outboxRepository.save(event);

            log.error("Event {} moved to DLQ", event.getEventId());
        }catch (Exception e){
            log.error("Failed to publish event {} to DLQ", event.getEventId(), e);
        }
    }

}
