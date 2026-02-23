package com.order.repository;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository  extends JpaRepository<Payment, UUID> {
    List<Payment> findByOrderIdOrderByCreatedAtDesc(UUID orderId);
}
