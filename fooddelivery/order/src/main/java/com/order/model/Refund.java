package com.order.model;


import com.order.domain.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refunds")
@Getter
@Setter
public class Refund {

    @Id
    private UUID refundId;

    @Column(nullable = false)
    private UUID orderId;

    private UUID paymentId;

    @Embedded
    private Money amount;

    private String reasonCode;
    private Instant createdAt;

    public static Refund of(UUID orderId, UUID paymentId, Money amount, String reasonCode) {
        Refund r = new Refund();
        r.refundId = UUID.randomUUID();
        r.orderId = orderId;
        r.paymentId = paymentId;
        r.amount = amount;
        r.reasonCode = reasonCode;
        r.createdAt = Instant.now();
        return r;
    }
}
