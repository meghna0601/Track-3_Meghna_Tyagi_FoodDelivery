package com.order.repository;

import com.order.model.OrderNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<OrderNote, UUID> {

    List<OrderNote> findByOrderIdOrderByCreatedAtDesc(UUID orderId);
}
