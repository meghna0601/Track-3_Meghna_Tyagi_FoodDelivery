package com.order.model;

import com.order.domain.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "order_lines")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineEntity {

    @Id
    private UUID orderLineId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private String skuId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "unit_currency")),
            @AttributeOverride(name = "amountMinor", column = @Column(name = "unit_amount_minor"))
    })
    private Money unitPrice;

    public static OrderLineEntity of(String skuId, String name, int quantity, Money unitPrice) {
        var l = new OrderLineEntity();
        l.orderLineId = UUID.randomUUID();
        l.skuId = skuId;
        l.name = name;
        l.quantity = quantity;
        l.unitPrice = unitPrice;
        return l;
    }

}
