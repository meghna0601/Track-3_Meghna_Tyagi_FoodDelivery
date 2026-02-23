package com.order.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Dtos {

    public record MoneyDto(@NotBlank String currency, @Min(0) long amountMinor) {}
    public record AddressDto(
            @NotBlank String line1,
            String line2,
            @NotBlank String city,
            @NotBlank String state,
            @NotBlank String postalCode,
            @NotBlank String country
    ) {}


    public record CreateOrderLineRequest(
            @NotBlank String skuId,
            @NotBlank String name,
            @Min(1) int quantity,
            @Valid @NotNull MoneyDto unitPrice
    ) {}

    public record CreateOrderRequest(
            @NotBlank String customerId,
            @Valid @NotEmpty List<CreateOrderLineRequest> items,
            @Valid @NotNull AddressDto shippingAddress,
            @Valid @NotNull AddressDto billingAddress,
            @NotBlank String shippingMethodId,
            String notes
    ) {}


    public record PatchOrderRequest(
            @Valid AddressDto shippingAddress,
            @Valid AddressDto billingAddress,
            String shippingMethodId,
            String notes
    ) {}

    public record OrderLineResponse(UUID orderLineId, String skuId, String name, int quantity, MoneyDto unitPrice) {}

    public record OrderResponse(
            UUID orderId,
            String orderNumber,
            String customerId,
            String status,
            MoneyDto grandTotal,
            String shippingMethodId,
            AddressDto shippingAddress,
            AddressDto billingAddress,
            String notes,
            List<OrderLineResponse> lines,
            Instant createdAt,
            Instant updatedAt,
            long version
    ) {}

    public record PageResponse<T>(List<T> items, int limit, long offset, long total) {}

    public record QuoteResponse(MoneyDto grandTotal, List<String> warnings) {}

    public record CancelOrderRequest(@NotBlank String reasonCode, String reasonText) {}

    public record CreatePaymentRequest(@NotBlank String paymentMethodRef, @Valid @NotNull MoneyDto amount) {}
    public record PaymentResponse(UUID paymentId, UUID orderId, String state, MoneyDto amount, String paymentMethodRef, Instant createdAt) {}

    public record CreateShipmentRequest(@NotBlank String warehouseId, @NotBlank String carrier) {}
    public record PatchShipmentRequest(String carrier, String trackingNumber, String status) {}

    public record ShipmentResponse(UUID shipmentId, UUID orderId, String status, String warehouseId, String carrier, String trackingNumber, Instant createdAt, Instant updatedAt) {}

    public record CreateReturnRequest(@NotBlank String reasonCode, @NotBlank String refundMethod) {}
    public record ReturnResponse(UUID returnId, UUID orderId, String status, String reasonCode, String refundMethod, Instant createdAt) {}

    public record CreateRefundRequest(UUID paymentId, @Valid @NotNull MoneyDto amount, @NotBlank String reasonCode) {}
    public record RefundResponse(UUID refundId, UUID orderId, UUID paymentId, MoneyDto amount, String reasonCode, Instant createdAt) {}

    public record AddNoteRequest(@NotBlank String note, @NotBlank String actor) {}
    public record NoteResponse(UUID noteId, UUID orderId, String note, String actor, Instant createdAt) {}

    public record AuditResponse(UUID auditId, UUID orderId, String eventType, String actor, String details, Instant createdAt) {}

    public record TransitionRequest(@NotBlank String fromStatus, @NotBlank String toStatus, @NotBlank String actor, String reasonCode, String metadata) {}
}
