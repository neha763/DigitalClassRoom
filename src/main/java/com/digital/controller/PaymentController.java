package com.digital.controller;

import com.digital.dto.GatewayResponseDTO;
import com.digital.dto.PaymentDTO;
import com.digital.servicei.MockGatewayService;
import com.digital.servicei.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final MockGatewayService mockGatewayService;

    public PaymentController(PaymentService paymentService, MockGatewayService mockGatewayService) {
        this.paymentService = paymentService;
        this.mockGatewayService = mockGatewayService;
    }

    // For direct payments (cash/card/upi)
    @PostMapping("/pay")
    public ResponseEntity<PaymentDTO> pay(@Valid @RequestBody PaymentDTO dto) {
        PaymentDTO p = paymentService.makePayment(dto);
        return ResponseEntity.status(201).body(p);
    }

    // Get payments by student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> byStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(paymentService.getPaymentsByStudent(studentId));
    }

    // PUT /api/payment/{paymentId} → update payment (e.g., refund, adjust)
    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> updatePayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody PaymentDTO dto) {
        PaymentDTO updated = paymentService.updatePayment(paymentId, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/payment/{paymentId} → delete/cancel payment
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<String> deletePayment(@PathVariable Long paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok("Payment with ID " + paymentId + " deleted successfully.");
    }
}
