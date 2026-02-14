package com.ecommerce.inventoryservice.repository;

import com.ecommerce.inventoryservice.entity.InventoryReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryReservationRepository extends JpaRepository<InventoryReservation , Long> {
}
