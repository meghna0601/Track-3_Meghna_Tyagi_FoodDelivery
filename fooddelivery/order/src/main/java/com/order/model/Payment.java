package com.order.model;

import com.order.domain.Money;
import com.order.enums.PaymentState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

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
        p.paymentId = UUID.randomUUID();
        p.orderId = orderId;
        p.amount = amount;
        p.paymentMethodRef = methodRef;
        p.state = PaymentState.INITIATED;
        p.createdAt = Instant.now();
        return p;
    }
}
