package com.order.model;


import com.order.enums.ReturnStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "returns")
@Data
public class Return {

    @Id
    private UUID returnId;

    @Column(nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReturnStatus status;

    private String reasonCode;
    private String refundMethod;
    private Instant createdAt;


    public static Return request(UUID orderId, String reasonCode, String refundMethod) {
        Return r = new Return();
        r.returnId = UUID.randomUUID();
        r.orderId = orderId;
        r.reasonCode = reasonCode;
        r.refundMethod = refundMethod;
        r.status = ReturnStatus.REQUESTED;
        r.createdAt = Instant.now();
        return r;
    }
}
