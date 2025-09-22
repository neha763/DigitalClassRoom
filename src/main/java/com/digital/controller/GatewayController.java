package com.digital.controller;

import com.digital.dto.GatewayResponseDTO;
import com.digital.servicei.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final PaymentService paymentService;

    public GatewayController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // POST /gateway/create-order → Mock order creation
    @PostMapping("/create-order")
    public ResponseEntity<Map<String, String>> createOrder(
            @RequestParam Long invoiceId,
            @RequestParam Double amount) {
        Map<String, String> response = new HashMap<>();
        response.put("orderId", "order_" + UUID.randomUUID());
        response.put("amount", amount.toString());
        response.put("upiLink", "upi://pay?orderId=" + response.get("orderId"));
        return ResponseEntity.ok(response);
    }

    // POST /gateway/callback → Mock payment callback
    @PostMapping("/callback")
    public ResponseEntity<GatewayResponseDTO> paymentCallback(
            @Valid @RequestBody GatewayResponseDTO response) {
        return ResponseEntity.ok(paymentService.processGatewayCallback(response));
    }

    // PUT /gateway/update-order → Mock update of order (amount, expiry, etc.)
    @PutMapping("/update-order/{orderId}")
    public ResponseEntity<Map<String, String>> updateOrder(
            @PathVariable String orderId,
            @RequestParam(required = false) Double newAmount,
            @RequestParam(required = false) String status) {
        Map<String, String> response = new HashMap<>();
        response.put("orderId", orderId);
        if (newAmount != null) {
            response.put("newAmount", newAmount.toString());
        }
        if (status != null) {
            response.put("status", status);
        }
        response.put("message", "Order updated successfully (mock).");
        return ResponseEntity.ok(response);
    }

    // DELETE /gateway/{orderId} → Mock cancel order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Map<String, String>> cancelOrder(@PathVariable String orderId) {
        Map<String, String> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("status", "CANCELLED");
        response.put("message", "Order cancelled successfully (mock).");
        return ResponseEntity.ok(response);
    }
}
