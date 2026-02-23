package com.order.model;


import com.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {

    @Id
    private UUID shipmentId;

    @Column(nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private String carrier;
    private String trackingNumber;
    private String warehouseId;

    private Instant createdAt;
    private Instant updatedAt;

    public static Shipment create(UUID orderId, String warehouseId, String carrier) {
        Shipment s = new Shipment();
        s.shipmentId = UUID.randomUUID();
        s.orderId = orderId;
        s.warehouseId = warehouseId;
        s.carrier = carrier;
        s.status = OrderStatus.CREATED;
        s.createdAt = Instant.now();
        s.updatedAt = s.createdAt;
        return s;
    }

    public void touch() { updatedAt = Instant.now(); }

    public void setCarrier(String carrier) { this.carrier = carrier; touch(); }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; touch(); }
    public void setStatus(OrderStatus status) { this.status = status; touch(); }
}
