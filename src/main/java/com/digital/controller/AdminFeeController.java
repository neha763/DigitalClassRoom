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
@RequestMapping("/api/admin")
public class AdminFeeController {

    private final FeeService feeService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;

    public AdminFeeController(FeeService feeService, InvoiceService invoiceService, PaymentService paymentService) {
        this.feeService = feeService;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
    }

    // Existing methods...

    // ------------------- FEE STRUCTURE -------------------

    // PUT /admin/fees/{id} → Update Fee Structure
    @PutMapping("/fees/{id}")
    public ResponseEntity<FeeStructureDTO> updateFeeStructure(
            @PathVariable Long id,
            @Valid @RequestBody FeeStructureDTO dto) {
        return ResponseEntity.ok(feeService.updateFeeStructure(id, dto));
    }


    // ------------------- INVOICE -------------------

    // PUT /admin/invoices/{id} → Update Invoice (e.g., dueDate or studentId)
    @PutMapping("/invoices/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(
            @PathVariable Long id,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String dueDate) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, studentId, dueDate));
    }

    // DELETE /admin/fees/{id} → Delete Fee Structure
    @DeleteMapping("/fees/{id}")
    public ResponseEntity<String> deleteFeeStructure(@PathVariable Long id) {
        feeService.deleteFeeStructure(id);
        return ResponseEntity.ok("Fee structure deleted successfully with id=" + id);
    }

    // DELETE /admin/invoices/{id} → Delete Invoice
    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok("Invoice deleted successfully with id=" + id);
    }

    // Existing methods...
    @PostMapping("/fees")
    public ResponseEntity<FeeStructureDTO> createFeeStructure(@Valid @RequestBody FeeStructureDTO dto) {
        return ResponseEntity.ok(feeService.createFeeStructure(dto));
    }

    @GetMapping("/fees/{classId}")
    public ResponseEntity<FeeStructureDTO> getFeeStructure(
            @PathVariable Long classId,
            @RequestParam String year) {
        return ResponseEntity.ok(feeService.getFeeStructureByClass(classId, year));
    }

    @PostMapping("/invoices")
    public ResponseEntity<InvoiceDTO> generateInvoice(
            @RequestParam Long studentId,
            @RequestParam Long feeId,
            @RequestParam String dueDate) {
        return ResponseEntity.ok(invoiceService.generateInvoice(studentId, feeId, dueDate));
    }

    @PutMapping("/invoices/{id}/adjust")
    public ResponseEntity<InvoiceDTO> adjustInvoice(
            @PathVariable Long id,
            @RequestParam double adjustment) {
        return ResponseEntity.ok(invoiceService.applyAdjustment(id, adjustment));
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getPaymentsByStudent(null));
    }
}
