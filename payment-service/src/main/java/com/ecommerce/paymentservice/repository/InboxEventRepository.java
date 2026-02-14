package com.ecommerce.paymentservice.repository;

import com.ecommerce.paymentservice.entity.InboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxEventRepository extends JpaRepository<InboxEvent, Long> {

    boolean existsByEventId(String eventId);

}
