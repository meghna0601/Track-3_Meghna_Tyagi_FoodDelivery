package com.order.controller;

import com.order.domain.Dtos;
import com.order.service.OrderAppService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderAppService app;


    @PostMapping("/orders")
    public Dtos.OrderResponse createOrder(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody Dtos.CreateOrderRequest req
    ) {
        return app.createOrder(req, idempotencyKey);
    }

    @GetMapping("/orders/{orderId}")
    public Dtos.OrderResponse getOrder(@PathVariable UUID orderId, @RequestParam String customerId) {
        return app.getOrderForCustomer(orderId, customerId);
    }

    @GetMapping("/orders")
    public Dtos.PageResponse<Dtos.OrderResponse> listOrders(
            @RequestParam String customerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") long offset
    ) {
        return app.listOrdersForCustomer(customerId, status, limit, offset);
    }

    @PatchMapping("/orders/{orderId}")
    public Dtos.OrderResponse patchOrder(
            @PathVariable UUID orderId,
            @RequestParam String customerId,
            @Valid @RequestBody Dtos.PatchOrderRequest req
    ) {
        return app.patchOrder(orderId, customerId, req);
    }

    @PostMapping("/orders/{orderId}/quote")
    public Dtos.QuoteResponse quote(@PathVariable UUID orderId, @RequestParam String customerId) {
        return app.quote(orderId, customerId);
    }

    @PostMapping("/orders/{orderId}/cancel")
    public Dtos.OrderResponse cancel(
            @PathVariable UUID orderId,
            @RequestParam String customerId,
            @Valid @RequestBody Dtos.CancelOrderRequest req
    ) {
        return app.cancelOrder(orderId, customerId, req);
    }

    @PostMapping("/orders/{orderId}/payments")
    public Dtos.PaymentResponse createPayment(
            @PathVariable UUID orderId,
            @RequestParam String customerId,
            @Valid @RequestBody Dtos.CreatePaymentRequest req
    ) {
        return app.createPayment(orderId, customerId, req);
    }

    @GetMapping("/orders/{orderId}/payments")
    public List<Dtos.PaymentResponse> listPayments(@PathVariable UUID orderId, @RequestParam String customerId) {
        return app.listPayments(orderId, customerId);
    }

    @GetMapping("/orders/{orderId}/shipments")
    public List<Dtos.ShipmentResponse> listShipments(@PathVariable UUID orderId, @RequestParam String customerId) {
        return app.listShipments(orderId, customerId);
    }

    @PostMapping("/orders/{orderId}/returns")
    public Dtos.ReturnResponse requestReturn(
            @PathVariable UUID orderId,
            @RequestParam String customerId,
            @Valid @RequestBody Dtos.CreateReturnRequest req
    ) {
        return app.requestReturn(orderId, customerId, req);
    }

    @GetMapping("/returns/{returnId}")
    public Dtos.ReturnResponse getReturn(@PathVariable UUID returnId) {
        return app.getReturn(returnId);
    }

    @GetMapping("/ops/orders")
    public Dtos.PageResponse<Dtos.OrderResponse> opsSearch(@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "0") long offset) {
        return app.opsSearchOrders(limit, offset);
    }

    @PostMapping("/ops/orders/{orderId}/transitions")
    public Dtos.OrderResponse opsTransition(@PathVariable UUID orderId, @Valid @RequestBody Dtos.TransitionRequest req) {
        return app.opsTransition(orderId, req);
    }

    @PostMapping("/ops/orders/{orderId}/cancel")
    public Dtos.OrderResponse opsCancel(
            @PathVariable UUID orderId,
            @RequestParam String actor,
            @Valid @RequestBody Dtos.CancelOrderRequest req
    ) {
        return app.opsCancel(orderId, req, actor);
    }

    @PostMapping("/ops/orders/{orderId}/shipments")
    public Dtos.ShipmentResponse opsCreateShipment(
            @PathVariable UUID orderId,
            @RequestParam String actor,
            @Valid @RequestBody Dtos.CreateShipmentRequest req
    ) {
        return app.opsCreateShipment(orderId, req, actor);
    }

    @PatchMapping("/ops/shipments/{shipmentId}")
    public Dtos.ShipmentResponse opsPatchShipment(
            @PathVariable UUID shipmentId,
            @RequestParam String actor,
            @Valid @RequestBody Dtos.PatchShipmentRequest req
    ) {
        return app.opsPatchShipment(shipmentId, req, actor);
    }

    @PostMapping("/ops/shipments/{shipmentId}/delivered")
    public Dtos.ShipmentResponse opsDelivered(@PathVariable UUID shipmentId, @RequestParam String actor) {
        return app.opsDelivered(shipmentId, actor);
    }

    @PostMapping("/ops/orders/{orderId}/refunds")
    public Dtos.RefundResponse opsRefund(
            @PathVariable UUID orderId,
            @RequestParam String actor,
            @Valid @RequestBody Dtos.CreateRefundRequest req
    ) {
        return app.opsRefund(orderId, req, actor);
    }

    @GetMapping("/ops/orders/{orderId}/refunds")
    public List<Dtos.RefundResponse> opsRefunds(@PathVariable UUID orderId) {
        return app.opsListRefunds(orderId);
    }

    @PostMapping("/ops/orders/{orderId}/notes")
    public Dtos.NoteResponse opsAddNote(@PathVariable UUID orderId, @Valid @RequestBody Dtos.AddNoteRequest req) {
        return app.opsAddNote(orderId, req.note(), req.actor());
    }

    @GetMapping("/ops/orders/{orderId}/audit")
    public List<Dtos.AuditResponse> opsAudit(@PathVariable UUID orderId) {
        return app.opsAudit(orderId);
    }
}
