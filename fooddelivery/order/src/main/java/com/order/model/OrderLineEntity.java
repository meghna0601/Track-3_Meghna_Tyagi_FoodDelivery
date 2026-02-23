package com.order.model;

import com.order.domain.Money;
import com.order.enums.PaymentState;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_lines")
public class OrderLineEntity {

    @Id
    private UUID paymentId;

    @Column(nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentState state;

    @Embedded
    private Money amount;

    private String paymentMethodRef;
    private Instant createdAt;

    protected PaymentEntity() {}

    public static PaymentEntity initiate(UUID orderId, Money amount, String methodRef) {
        PaymentEntity p = new PaymentEntity();
        p.paymentId = UUID.randomUUID();
        p.orderId = orderId;
        p.amount = amount;
        p.paymentMethodRef = methodRef;
        p.state = PaymentState.INITIATED;
        p.createdAt = Instant.now();
        return p;
    }

}
