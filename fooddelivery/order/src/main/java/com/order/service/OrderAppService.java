package com.order.service;

import com.order.domain.Address;
import com.order.domain.Dtos;
import com.order.domain.Money;
import com.order.enums.OrderStatus;
import com.order.enums.PaymentState;
import com.order.enums.ReturnStatus;
import com.order.exception.BadRequestException;
import com.order.exception.ConflictException;
import com.order.exception.NotFoundException;
import com.order.model.*;
import com.order.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderAppService {

    private final OrderRepository orders;
    private final PaymentRepository payments;
    private final ShipmentRepository shipments;
    private final ReturnRepository returns;
    private final RefundRepository refunds;
    private final NoteRepository notes;
    private final AuditRepository audit;
    private final IdempotencyRepository idempotency;

    @Transactional
    public Dtos.OrderResponse createOrder(Dtos.CreateOrderRequest req, String idempotencyKey) {
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            var existing = idempotency.findByKeyValueAndOperation(idempotencyKey, "CREATE_ORDER");
            if (existing.isPresent()) {
                UUID id = UUID.fromString(existing.get().getResourceId());
                return toOrderResponse(orders.findById(id).orElseThrow(() -> new NotFoundException("Order not found")));
            }
        }

        Order o = Order.create(req.customerId());
        o.setShippingAddress(toAddress(req.shippingAddress()));
        o.setBillingAddress(toAddress(req.billingAddress()));
        o.setShippingMethodId(req.shippingMethodId());
        o.setNotes(req.notes());

        o.clearLines();
        for (var item : req.items()) {
            o.addLine(OrderLineEntity.of(item.skuId(), item.name(), item.quantity(), toMoney(item.unitPrice())));
        }

        recalcTotals(o);
        orders.save(o);
        audit.save(AuditEvent.of(o.getOrderId(), "OrderCreated", req.customerId(), null));

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            idempotency.save(IdempotencyKey.of(idempotencyKey, "CREATE_ORDER", o.getOrderId().toString()));
        }

        return toOrderResponse(o);
    }

    @Transactional(readOnly = true)
    public Dtos.OrderResponse getOrderForCustomer(UUID orderId, String customerId) {
        var o = orders.findByOrderIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return toOrderResponse(o);
    }

    @Transactional(readOnly = true)
    public Dtos.PageResponse<Dtos.OrderResponse> listOrdersForCustomer(String customerId, String status, int limit, long offset) {
        var pageable = PageRequest.of((int) (offset / Math.max(1, limit)), limit, Sort.by("createdAt").descending());
        var page = (status == null || status.isBlank())
                ? orders.findByCustomerIdOrderByCreatedAtDesc(customerId, pageable)
                : orders.findByCustomerIdAndStatusOrderByCreatedAtDesc(customerId, OrderStatus.valueOf(status), pageable);

        return new Dtos.PageResponse<>(page.getContent().stream().map(this::toOrderResponse).toList(), limit, offset, page.getTotalElements());
    }

    @Transactional
    public Dtos.OrderResponse patchOrder(UUID orderId, String customerId, Dtos.PatchOrderRequest patch) {
        var o = orders.findByOrderIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (o.getStatus() != OrderStatus.PENDING) throw new ConflictException("Only DRAFT orders can be edited");

        if (patch.shippingAddress() != null) o.setShippingAddress(toAddress(patch.shippingAddress()));
        if (patch.billingAddress() != null) o.setBillingAddress(toAddress(patch.billingAddress()));
        if (patch.shippingMethodId() != null) o.setShippingMethodId(patch.shippingMethodId());
        if (patch.notes() != null) o.setNotes(patch.notes());

        recalcTotals(o);
        audit.save(AuditEvent.of(o.getOrderId(), "OrderPatched", customerId, null));
        return toOrderResponse(o);
    }

    @Transactional
    public Dtos.OrderResponse cancelOrder(UUID orderId, String customerId, Dtos.CancelOrderRequest req) {
        var o = orders.findByOrderIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (EnumSet.of(OrderStatus.SHIPPED, OrderStatus.DELIVERED, OrderStatus.PLACED).contains(o.getStatus())) {
            throw new ConflictException("Order cannot be cancelled after shipping");
        }

        o.setStatus(OrderStatus.CANCELLED);
        audit.save(AuditEvent.of(o.getOrderId(), "OrderCancelled", customerId, req.reasonCode() + ":" + req.reasonText()));
        return toOrderResponse(o);
    }

    @Transactional
    public Dtos.QuoteResponse quote(UUID orderId, String customerId) {
        var o = orders.findByOrderIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        recalcTotals(o);
        audit.save(AuditEvent.of(o.getOrderId(), "OrderQuoted", customerId, null));
        return new Dtos.QuoteResponse(toMoneyDto(o.getGrandTotal()), List.of());
    }

    @Transactional
    public Dtos.PaymentResponse createPayment(UUID orderId, String customerId, Dtos.CreatePaymentRequest req) {
        var o = orders.findByOrderIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        // Minimal demo rule: any payment initiation moves DRAFT -> PENDING_PAYMENT
        if (o.getStatus() == OrderStatus.CREATED) o.setStatus(PaymentState.CAPTURED);

        Payment p = Payment.initiate(orderId, toMoney(req.amount()), req.paymentMethodRef());
        // Demo: immediately authorize
        p.setState(PaymentState.AUTHORIZED);
        payments.save(p);

        audit.save(AuditEvent.of(o.getOrderId(), "PaymentAuthorized", customerId, p.getPaymentId().toString()));
        return toPaymentResponse(p);
    }

    @Transactional(readOnly = true)
    public List<Dtos.PaymentResponse> listPayments(UUID orderId, String customerId) {
        // ensure order belongs to customer
        orders.findByOrderIdAndCustomerId(orderId, customerId).orElseThrow(() -> new NotFoundException("Order not found"));
        return payments.findByOrderIdOrderByCreatedAtDesc(orderId).stream().map(this::toPaymentResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<Dtos.ShipmentResponse> listShipments(UUID orderId, String customerId) {
        orders.findByOrderIdAndCustomerId(orderId, customerId).orElseThrow(() -> new NotFoundException("Order not found"));
        return shipments.findByOrderIdOrderByCreatedAtDesc(orderId).stream().map(this::toShipmentResponse).toList();
    }

    @Transactional
    public Dtos.ReturnResponse requestReturn(UUID orderId, String customerId, Dtos.CreateReturnRequest req) {
        var o = orders.findByOrderIdAndCustomerId(orderId, customerId).orElseThrow(() -> new NotFoundException("Order not found"));
        if (!EnumSet.of(OrderStatus.DELIVERED, OrderStatus.SHIPPED).contains(o.getStatus())) {
            throw new ConflictException("Returns allowed only after delivery");
        }
        var r = Return.request(orderId, req.reasonCode(), req.refundMethod());
        returns.save(r);
        o.setStatus(ReturnStatus.REQUESTED);
        audit.save(AuditEvent.of(o.getOrderId(), "ReturnRequested", customerId, r.getReturnId().toString()));
        return toReturnResponse(r);
    }

    @Transactional(readOnly = true)
    public Dtos.ReturnResponse getReturn(UUID returnId) {
        var r = returns.findById(returnId).orElseThrow(() -> new NotFoundException("Return not found"));
        return toReturnResponse(r);
    }

    // ---------- Ops APIs ----------

    @Transactional(readOnly = true)
    public Dtos.PageResponse<Dtos.OrderResponse> opsSearchOrders(int limit, long offset) {
        var pageable = PageRequest.of((int) (offset / Math.max(1, limit)), limit, Sort.by("createdAt").descending());
        var page = orders.findAll(pageable);
        return new Dtos.PageResponse<>(page.getContent().stream().map(this::toOrderResponse).toList(), limit, offset, page.getTotalElements());
    }

    @Transactional
    public Dtos.OrderResponse opsTransition(UUID orderId, Dtos.TransitionRequest req) {
        var o = orders.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        var from = OrderStatus.valueOf(req.fromStatus());
        var to = OrderStatus.valueOf(req.toStatus());
        if (o.getStatus() != from) throw new ConflictException("Order is not in fromStatus=" + from);

        if (!allowedTransitions().getOrDefault(from, Set.of()).contains(to)) {
            throw new BadRequestException("Illegal transition " + from + " -> " + to);
        }

        o.setStatus(to);
        audit.save(AuditEvent.of(o.getOrderId(), "OrderTransition", req.actor(), from + "->" + to + " reason=" + req.reasonCode()));
        return toOrderResponse(o);
    }

    @Transactional
    public Dtos.OrderResponse opsCancel(UUID orderId, Dtos.CancelOrderRequest req, String actor) {
        var o = orders.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        o.setStatus(OrderStatus.CANCELLED);
        audit.save(AuditEvent.of(o.getOrderId(), "OrderCancelledOps", actor, req.reasonCode() + ":" + req.reasonText()));
        return toOrderResponse(o);
    }

    @Transactional
    public Dtos.ShipmentResponse opsCreateShipment(UUID orderId, Dtos.CreateShipmentRequest req, String actor) {
        var o = orders.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        var s = Shipment.create(orderId, req.warehouseId(), req.carrier());
        shipments.save(s);

        // Demo: shipment creation pushes status forward
        if (o.getStatus() == OrderStatus.PLACED || o.getStatus() == OrderStatus.CREATED || o.getStatus() == OrderStatus.PENDING) {
            o.setStatus(OrderStatus.SHIPPED);
        }

        audit.save(AuditEvent.of(o.getOrderId(), "ShipmentCreated", actor, s.getShipmentId().toString()));
        return toShipmentResponse(s);
    }

    @Transactional
    public Dtos.ShipmentResponse opsPatchShipment(UUID shipmentId, Dtos.PatchShipmentRequest req, String actor) {
        var s = shipments.findById(shipmentId).orElseThrow(() -> new NotFoundException("Shipment not found"));

        if (req.carrier() != null) s.setCarrier(req.carrier());
        if (req.trackingNumber() != null) s.setTrackingNumber(req.trackingNumber());
        if (req.status() != null) s.setStatus(OrderStatus.valueOf(req.status()));

        audit.save(AuditEvent.of(s.getOrderId(), "ShipmentPatched", actor, shipmentId.toString()));
        return toShipmentResponse(s);
    }

    @Transactional
    public Dtos.ShipmentResponse opsDelivered(UUID shipmentId, String actor) {
        var s = shipments.findById(shipmentId).orElseThrow(() -> new NotFoundException("Shipment not found"));
        s.setStatus(OrderStatus.DELIVERED);

        var o = orders.findById(s.getOrderId()).orElseThrow(() -> new NotFoundException("Order not found"));
        o.setStatus(OrderStatus.DELIVERED);

        audit.save(AuditEvent.of(o.getOrderId(), "ShipmentDelivered", actor, shipmentId.toString()));
        return toShipmentResponse(s);
    }

    @Transactional
    public Dtos.RefundResponse opsRefund(UUID orderId, Dtos.CreateRefundRequest req, String actor) {
        var o = orders.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        var refund = Refund.of(orderId, req.paymentId(), toMoney(req.amount()), req.reasonCode());
        refunds.save(refund);
        o.setStatus(ReturnStatus.REFUNDED);
        audit.save(AuditEvent.of(o.getOrderId(), "RefundIssued", actor, refund.getRefundId().toString()));
        return toRefundResponse(refund);
    }

    @Transactional(readOnly = true)
    public List<Dtos.RefundResponse> opsListRefunds(UUID orderId) {
        orders.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        return refunds.findByOrderIdOrderByCreatedAtDesc(orderId).stream().map(this::toRefundResponse).toList();
    }

    @Transactional
    public Dtos.NoteResponse opsAddNote(UUID orderId, String note, String actor) {
        orders.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        var n = OrderNote.of(orderId, note, actor);
        notes.save(n);
        audit.save(AuditEvent.of(orderId, "NoteAdded", actor, n.getNoteId().toString()));
        return toNoteResponse(n);
    }

    @Transactional(readOnly = true)
    public List<Dtos.AuditResponse> opsAudit(UUID orderId) {
        orders.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        return audit.findByOrderIdOrderByCreatedAtDesc(orderId).stream().map(this::toAuditResponse).toList();
    }

    // ---------- Helpers ----------

    private void recalcTotals(Order o) {
        // Minimal demo pricing: sum(qty * unitPrice)
        if (o.getLines().isEmpty()) {
            o.setGrandTotal(new Money("USD", 0));
            return;
        }
        String currency = o.getLines().getFirst().getUnitPrice().getCurrency();
        long total = 0;
        for (var l : o.getLines()) total += (long) l.getQuantity() * l.getUnitPrice().getAmountMinor();
        o.setGrandTotal(new Money(currency, total));
    }

    private Map<OrderStatus, Set<OrderStatus>> allowedTransitions() {
        return Map.of(
                OrderStatus.DRAFT, Set.of(OrderStatus.PENDING_PAYMENT, OrderStatus.CANCELLED),
                PaymentState.INITIATED, Set.of(OrderStatus.CREATED, OrderStatus.CANCELLED),
                OrderStatus.CREATED, Set.of(OrderStatus.PLACED, OrderStatus.CANCELLED),
                OrderStatus.PLACED, Set.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED),
                OrderStatus.SHIPPED, Set.of(OrderStatus.DELIVERED),
                OrderStatus.DELIVERED, Set.of(OrderStatus.COMPLETED, ReturnStatus.RETURN_REQUESTED),
                OrderStatus.RETURN_REQUESTED, Set.of(OrderStatus.RETURNED, OrderStatus.REFUNDED),
                OrderStatus.RETURNED, Set.of(OrderStatus.REFUNDED)
        );
    }

    private Money toMoney(Money dto) { return new Money(dto.currency(), dto.amountMinor()); }
    private Dtos.MoneyDto toMoneyDto(Money m) { return new Money(m.getCurrency(), m.getAmountMinor()); }
    private Address toAddress(Dtos.AddressDto dto) { return new Address(dto.line1(), dto.line2(), dto.city(), dto.state(), dto.postalCode(), dto.country()); }
    private Dtos.AddressDto toAddressDto(Address a) { return new Dtos.AddressDto(a.getLine1(), a.getLine2(), a.getCity(), a.getState(), a.getPostalCode(), a.getCountry()); }

    private Dtos.OrderResponse toOrderResponse(Order o) {
        var lines = o.getLines().stream()
                .map(l -> new Dtos.OrderLineResponse(l.getOrderLineId(), l.getSkuId(), l.getName(), l.getQuantity(), toMoneyDto(l.getUnitPrice())))
                .toList();

        return new Dtos.OrderResponse(
                o.getOrderId(),
                o.getOrderNumber(),
                o.getCustomerId(),
                o.getStatus().name(),
                toMoneyDto(o.getGrandTotal()),
                o.getShippingMethodId(),
                o.getShippingAddress() == null ? null : toAddressDto(o.getShippingAddress()),
                o.getBillingAddress() == null ? null : toAddressDto(o.getBillingAddress()),
                o.getNotes(),
                lines,
                o.getCreatedAt(),
                o.getUpdatedAt(),
                o.getVersion()
        );
    }

    private Dtos.PaymentResponse toPaymentResponse(Payment p) {
        return new Dtos.PaymentResponse(p.getPaymentId(), p.getOrderId(), p.getState().name(), toMoneyDto(p.getAmount()), p.getPaymentMethodRef(), p.getCreatedAt());
    }

    private Dtos.ShipmentResponse toShipmentResponse(Shipment s) {
        return new Dtos.ShipmentResponse(s.getShipmentId(), s.getOrderId(), s.getStatus().name(), s.getWarehouseId(), s.getCarrier(), s.getTrackingNumber(), s.getCreatedAt(), s.getUpdatedAt());
    }

    private Dtos.ReturnResponse toReturnResponse(Return r) {
        return new Dtos.ReturnResponse(r.getReturnId(), r.getOrderId(), r.getStatus().name(), r.getReasonCode(), r.getRefundMethod(), r.getCreatedAt());
    }

    private Dtos.RefundResponse toRefundResponse(Refund r) {
        return new Dtos.RefundResponse(r.getRefundId(), r.getOrderId(), r.getPaymentId(), toMoneyDto(r.getAmount()), r.getReasonCode(), r.getCreatedAt());
    }

    private Dtos.NoteResponse toNoteResponse(OrderNote n) {
        return new Dtos.NoteResponse(n.getNoteId(), n.getOrderId(), n.getNote(), n.getActor(), n.getCreatedAt());
    }

    private Dtos.AuditResponse toAuditResponse(AuditEvent a) {
        return new Dtos.AuditResponse(a.getAuditId(), a.getOrderId(), a.getEventType(), a.getActor(), a.getDetails(), a.getCreatedAt());
    }
}
