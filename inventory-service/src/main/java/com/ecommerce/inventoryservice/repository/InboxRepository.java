package com.ecommerce.inventoryservice.repository;

import com.ecommerce.inventoryservice.entity.InboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxRepository extends JpaRepository<InboxEvent,Long> {

    boolean existsByEventId(String eventId);

}
