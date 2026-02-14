package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox , String> {

    List<Outbox> findTop50ByStatusInOrderByCreatedAtAsc(List<String> statuses);

}
