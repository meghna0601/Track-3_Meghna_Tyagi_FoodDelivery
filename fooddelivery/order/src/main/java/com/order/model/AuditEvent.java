package com.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "order_audit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuditEvent {

    @Id
    private UUID auditId;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String actor;

    @Column(length = 4000)
    private String details;

    private Instant createdAt;

    public static AuditEvent of(UUID orderId, String eventType, String actor, String details) {
        AuditEvent a = new AuditEvent();
        a.auditId = UUID.randomUUID();
        a.orderId = orderId;
        a.eventType = eventType;
        a.actor = actor;
        a.details = details;
        a.createdAt = Instant.now();
        return a;
    }
}
