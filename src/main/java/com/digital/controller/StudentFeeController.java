package com.digital.controller;

import com.digital.dto.FeeStructureDTO;
import com.digital.dto.InvoiceDTO;
import com.digital.dto.PaymentDTO;
import com.digital.servicei.FeeService;
import com.digital.servicei.InvoiceService;
import com.digital.servicei.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studentFee")
public class StudentFeeController {

    private final FeeService feeService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;

    public StudentFeeController(FeeService feeService, InvoiceService invoiceService, PaymentService paymentService) {
        this.feeService = feeService;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
    }

    // ================= Fee Structure =================

    // GET /student/fees → View All Fee Structures
    @GetMapping("/fees")
    public ResponseEntity<List<FeeStructureDTO>> getAllFeeStructures() {
        return ResponseEntity.ok(feeService.getAllFeeStructures());
    }

    // ================= Invoices =================

    // GET /student/invoices → View Student Invoices
    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceDTO>> getInvoices(@RequestParam Long studentId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByStudent(studentId));
    }

    // GET /student/invoices/{id}/receipt → Download Receipt (for now return InvoiceDTO)
    @GetMapping("/invoices/{id}/receipt")
    public ResponseEntity<InvoiceDTO> getReceipt(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoice(id));
    }

    // ================= Payments =================

    // POST /student/payments → Pay Invoice
    @PostMapping("/payments")
    public ResponseEntity<PaymentDTO> makePayment(@Valid @RequestBody PaymentDTO dto) {
        return ResponseEntity.ok(paymentService.makePayment(dto));
    }

    // GET /student/payments/history → View Past Payments
    @GetMapping("/payments/history")
    public ResponseEntity<List<PaymentDTO>> getPaymentHistory(@RequestParam Long studentId) {
        return ResponseEntity.ok(paymentService.getPaymentsByStudent(studentId));
    }

    // PUT /student/payments/{id} → Update Payment (e.g., change payment mode or status)
    @PutMapping("/payments/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentDTO dto) {
        return ResponseEntity.ok(paymentService.updatePayment(id, dto));
    }

    // DELETE /student/payments/{id} → Cancel/Delete Payment
    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
