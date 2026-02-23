package com.order.repository;

import com.order.enums.OrderStatus;
import com.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderIdAndCustomerId(UUID orderId, String customerId);

    Page<Order> findByCustomerIdOrderByCreatedAtDesc(String customerId, Pageable pageable);

    Page<Order> findByCustomerIdAndStatusOrderByCreatedAtDesc(String customerId, OrderStatus status, Pageable pageable);

    Page<Order> findByCreatedAtBetweenOrderByCreatedAtDesc(Instant from, Instant to, Pageable pageable);
}
