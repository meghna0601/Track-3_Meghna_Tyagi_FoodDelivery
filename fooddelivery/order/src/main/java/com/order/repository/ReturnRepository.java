package com.order.repository;

import com.order.model.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReturnRepository extends JpaRepository<Return, UUID> {

    Optional<Return> findByReturnId(UUID returnId);
}
