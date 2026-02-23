package com.order.model;


import com.order.domain.Address;
import com.order.domain.Money;
import com.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    private UUID orderId;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "line1", column = @Column(name = "ship_line1")),
            @AttributeOverride(name = "line2", column = @Column(name = "ship_line2")),
            @AttributeOverride(name = "city", column = @Column(name = "ship_city")),
            @AttributeOverride(name = "state", column = @Column(name = "ship_state")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "ship_postal")),
            @AttributeOverride(name = "country", column = @Column(name = "ship_country"))
    })
    private Address shippingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "line1", column = @Column(name = "bill_line1")),
            @AttributeOverride(name = "line2", column = @Column(name = "bill_line2")),
            @AttributeOverride(name = "city", column = @Column(name = "bill_city")),
            @AttributeOverride(name = "state", column = @Column(name = "bill_state")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "bill_postal")),
            @AttributeOverride(name = "country", column = @Column(name = "bill_country"))
    })
    private Address billingAddress;

    private String shippingMethodId;
    private String notes;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "total_currency")),
            @AttributeOverride(name = "amountMinor", column = @Column(name = "total_amount_minor"))
    })
    private Money grandTotal;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderLineEntity> lines = new ArrayList<>();

    private Instant createdAt;
    private Instant updatedAt;

    @Version
    private long version;

    public static Order create(String customerId) {
        Order o = new Order();
        o.orderId = UUID.randomUUID();
        o.orderNumber = "ORD-" + o.orderId.toString().substring(0, 8).toUpperCase();
        o.customerId = customerId;
        o.status = OrderStatus.PENDING;
        o.grandTotal = new Money("USD", 0);
        o.createdAt = Instant.now();
        o.updatedAt = o.createdAt;
        return o;
    }

    public void touch() { this.updatedAt = Instant.now(); }

    public void clearLines() { this.lines.clear(); touch(); }

    public void addLine(OrderLineEntity line) {
        line.setOrder(this);
        this.lines.add(line);
        touch();
    }
}
