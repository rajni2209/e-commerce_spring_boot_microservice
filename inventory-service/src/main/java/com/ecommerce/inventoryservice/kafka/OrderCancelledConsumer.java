package com.ecommerce.inventoryservice.kafka;

import com.ecommerce.inventoryservice.event.OrderCancelledEvent;
import com.ecommerce.inventoryservice.event.OrderItemEvent;
import com.ecommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCancelledConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "order-cancelled-events",
            groupId =  "inventory-service-group"
    )
    public void onOrderCancelled(OrderCancelledEvent event) {
        inventoryService.release(event.getOrderId());
        log.info("Inventory released for cancelled order={}", event.getOrderId());
    }
}
