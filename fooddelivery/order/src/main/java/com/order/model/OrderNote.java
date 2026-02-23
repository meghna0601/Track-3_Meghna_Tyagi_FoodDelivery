package com.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_notes")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderNote {
    @Id
    private UUID noteId;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false, length = 4000)
    private String note;

    @Column(nullable = false)
    private String actor;

    private Instant createdAt;


    public static OrderNote of(UUID orderId, String note, String actor) {
        OrderNote n = new OrderNote();
        n.noteId = UUID.randomUUID();
        n.orderId = orderId;
        n.note = note;
        n.actor = actor;
        n.createdAt = Instant.now();
        return n;
    }

}
