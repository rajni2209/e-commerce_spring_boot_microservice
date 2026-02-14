package com.ecommerce.inventoryservice.repository;

import com.ecommerce.inventoryservice.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent , String> {

    List<OutboxEvent> findTop50ByStatusInOrderByCreatedAtAsc(List<String> statuses);

}
