package com.order.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "idempotency_keys", uniqueConstraints = @UniqueConstraint(columnNames = {"key_value"}))
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IdempotencyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key_value", nullable = false, length = 128)
    private String keyValue;

    @Column(nullable = false)
    private String operation;

    @Column(nullable = false, length = 64)
    private String resourceId; // store orderId UUID as string

    private Instant createdAt;

    public static IdempotencyKey of(String keyValue, String operation, String resourceId) {
        IdempotencyKey e = new IdempotencyKey();
        e.keyValue = keyValue;
        e.operation = operation;
        e.resourceId = resourceId;
        e.createdAt = Instant.now();
        return e;
    }
}
