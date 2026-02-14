package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.entity.Inventory;
import com.ecommerce.inventoryservice.entity.InventoryReservation;
import com.ecommerce.inventoryservice.entity.ReservedItem;
import com.ecommerce.inventoryservice.event.OrderItemEvent;
import com.ecommerce.inventoryservice.exception.InsufficientStockException;
import com.ecommerce.inventoryservice.exception.ProductNotFoundException;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import com.ecommerce.inventoryservice.repository.InventoryReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryReservationRepository reservationRepository;

    @Transactional
    public void reserved(List<OrderItemEvent> itemEvents , Long orderId){
        if (alreadyReservedForOrder(orderId)){
            log.info("Order {} already reserved. Skipping.", orderId);
            return;
        }
        for (OrderItemEvent item : itemEvents){
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(ProductNotFoundException::new);
            if (inventory.getAvailableQuantity() < item.getQuantity()){
                throw new InsufficientStockException();
            }
        }

        for (OrderItemEvent item : itemEvents){
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(ProductNotFoundException::new);
            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity() - item.getQuantity()
            );
            inventory.setReservedQuantity(
                    inventory.getReservedQuantity() + item.getQuantity()
            );
        }
        InventoryReservation reservation = new InventoryReservation();
        reservation.setOrderId(orderId);
        List<ReservedItem> reservedItems = itemEvents.stream()
                .map(i -> new ReservedItem(
                        null,
                        i.getProductId(),
                        i.getQuantity()
                ))
                .toList();
        reservation.setItems(reservedItems);
        reservationRepository.save(reservation);
    }


    @Transactional
    public void release(Long orderId){

        InventoryReservation reservation =
                reservationRepository.findById(orderId).orElse(null);
        if (reservation == null) {
            log.info("No inventory reservation found for order {}. Skipping release.", orderId);
            return;
        }
        for (ReservedItem item : reservation.getItems()) {

            Inventory inventory =
                    inventoryRepository.findByProductId(item.getProductId()).orElse(null);
            if (inventory == null) {
                log.warn("Inventory not found for product {} while releasing order {}",
                        item.getProductId(), orderId);
                continue;
            }
            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity() + item.getQuantity()
            );
            inventory.setReservedQuantity(
                    inventory.getReservedQuantity() - item.getQuantity()
            );
        }
        reservationRepository.delete(reservation);
    }


    private boolean alreadyReservedForOrder(Long orderId) {
        return reservationRepository.existsById(orderId);
    }
}
