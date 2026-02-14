package com.ecommerce.inventoryservice.kafka;

import com.ecommerce.inventoryservice.event.EventEnvelope;
import com.ecommerce.inventoryservice.event.InventoryFailedEvent;
import com.ecommerce.inventoryservice.event.InventoryReservedEvent;
import com.ecommerce.inventoryservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final OutboxService outboxService;

    private static final String TOPIC = "inventory-reserved-events";
    private static final String TOPIC2 = "inventory-failed-events";

    public void publishReserved(Long orderId){
        EventEnvelope<InventoryReservedEvent> envelope = new EventEnvelope<>(
                UUID.randomUUID().toString(),
                "InventoryReservedEvent",
                orderId,
                Instant.now(),
                1,
                new InventoryReservedEvent(orderId)
        );
        outboxService.saveData(TOPIC , envelope);
    }

    public void publishFailed(Long orderId , String reason){
        EventEnvelope<InventoryFailedEvent> env =
                new EventEnvelope<>(
                        UUID.randomUUID().toString(),
                        "InventoryFailedEvent",
                        orderId,
                        Instant.now(),
                        1,
                        new InventoryFailedEvent(orderId, reason)
                );

        outboxService.saveData(TOPIC2, env);
    }

}
