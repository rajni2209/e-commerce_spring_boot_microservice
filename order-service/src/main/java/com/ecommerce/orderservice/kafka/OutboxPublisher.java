package com.ecommerce.orderservice.kafka;

import com.ecommerce.orderservice.entity.Outbox;
import com.ecommerce.orderservice.repository.OutboxRepository;
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
    private static final String DLQ_TOPIC = "order-outbox-dlq";

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void publish(){
        List<Outbox> events = outboxRepository.findTop50ByStatusInOrderByCreatedAtAsc(List.of("NEW" , "FAILED"));

        for (Outbox e : events){

            if (e.getAttempts() >= MAX_RETRIES){
                moveToDead(e);
                continue;
            }

            try {
                e.setStatus("IN_PROGRESS");
                outboxRepository.save(e);

                kafkaTemplate.send(e.getTopic() , String.valueOf(e.getAggregatedId()) , e.getPayLoad()).get();
                e.setStatus("SENT");
                e.setSentAt(Instant.now());
                e.setAttempts(e.getAttempts() + 1);
                outboxRepository.save(e);

                log.info("Outbox published eventId={} topic={}", e.getEventId(), e.getTopic());
            }catch (Exception exception){
                e.setStatus("FAILED");
                e.setAttempts(e.getAttempts() + 1);
                outboxRepository.save(e);
                log.error("Outbox publish failed eventId={} topic={}", e.getEventId(), e.getTopic(), exception);
            }
        }
    }

    public void moveToDead(Outbox event){
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
