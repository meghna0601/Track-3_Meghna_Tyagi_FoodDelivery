package com.order.model;

import com.order.domain.Money;
import com.order.enums.PaymentState;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_lines")
@Data
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

    public static Payment initiate(UUID orderId, Money amount, String methodRef) {
        Payment p = new Payment();
        p.setPaymentId(UUID.randomUUID());
        p.setOrderId(orderId);
        p.setAmount(amount);
        p.setPaymentMethodRef(methodRef);
        p.setState(PaymentState.INITIATED);
        p.setCreatedAt(Instant.now());
        return p;
    }

}
