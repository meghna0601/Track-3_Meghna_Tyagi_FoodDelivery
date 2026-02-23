package com.order.repository;

import com.order.model.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditRepository extends JpaRepository<AuditEvent, UUID> {

    List<AuditEvent> findByOrderIdOrderByCreatedAtDesc(UUID orderId);
}
