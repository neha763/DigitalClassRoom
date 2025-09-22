package com.digital.serviceimpl;

import com.digital.dto.GatewayResponseDTO;
import com.digital.dto.PaymentDTO;
import com.digital.entity.Invoice;
import com.digital.entity.Payment;
import com.digital.enums.PaymentMode;
import com.digital.enums.PaymentStatus;
import com.digital.repository.InvoiceRepository;
import com.digital.repository.PaymentRepository;
import com.digital.servicei.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final InvoiceRepository invoiceRepo;

    public PaymentServiceImpl(PaymentRepository paymentRepo, InvoiceRepository invoiceRepo) {
        this.paymentRepo = paymentRepo;
        this.invoiceRepo = invoiceRepo;
    }

    @Override
    public PaymentDTO makePayment(PaymentDTO dto) {
        Invoice inv = invoiceRepo.findById(dto.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        BigDecimal remaining = inv.getTotalDue().subtract(inv.getAmountPaid());
        if (dto.getAmountPaid().compareTo(remaining) > 0) {
            throw new RuntimeException("Payment exceeds remaining due amount");
        }

        Payment payment = new Payment();
        payment.setInvoiceId(dto.getInvoiceId());
        payment.setStudentId(dto.getStudentId());
        payment.setAmountPaid(dto.getAmountPaid());
        payment.setPaymentMode(PaymentMode.valueOf(dto.getPaymentMode()));
        payment.setGatewayReferenceId(dto.getGatewayReferenceId());
        payment.setStatus(PaymentStatus.PENDING);

        Payment saved = paymentRepo.save(payment);

        // If cash/card/UPI (not gateway) → mark as success immediately
        if (!payment.getPaymentMode().equals(PaymentMode.PAYMENT_GATEWAY)) {
            saved.setStatus(PaymentStatus.SUCCESS);
            inv.setAmountPaid(inv.getAmountPaid().add(saved.getAmountPaid()));
            inv.recomputeStatus();
            invoiceRepo.save(inv);
        }

        return toDTO(saved);
    }

    @Override
    public List<PaymentDTO> getPaymentsByStudent(Long studentId) {
        return paymentRepo.findByStudentId(studentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GatewayResponseDTO processGatewayCallback(GatewayResponseDTO response) {
        Payment pay = paymentRepo.findByTransactionId(response.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Payment not found for txn: " + response.getTransactionId()));

        if ("SUCCESS".equalsIgnoreCase(response.getStatus())) {
            pay.setStatus(PaymentStatus.SUCCESS);
            Invoice inv = invoiceRepo.findById(pay.getInvoiceId())
                    .orElseThrow(() -> new RuntimeException("Invoice not found"));
            inv.setAmountPaid(inv.getAmountPaid().add(pay.getAmountPaid()));
            inv.recomputeStatus();
            invoiceRepo.save(inv);
        } else {
            pay.setStatus(PaymentStatus.FAILED);
        }

        paymentRepo.save(pay);
        return response;
    }

    // 🔹 New method: update payment (e.g., refund, adjust)
    @Override
    public PaymentDTO updatePayment(Long paymentId, PaymentDTO dto) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        // Example update: allow refund or correction of status/amount
        if (dto.getStatus() != null) {
            payment.setStatus(PaymentStatus.valueOf(dto.getStatus()));
        }
        if (dto.getAmountPaid() != null) {
            payment.setAmountPaid(dto.getAmountPaid());
        }
        if (dto.getPaymentMode() != null) {
            payment.setPaymentMode(PaymentMode.valueOf(dto.getPaymentMode()));
        }

        Payment updated = paymentRepo.save(payment);
        return toDTO(updated);
    }

    // 🔹 New method: delete payment (admin only in real system)
    @Override
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        paymentRepo.delete(payment);

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            Invoice inv = invoiceRepo.findById(payment.getInvoiceId())
                    .orElseThrow(() -> new RuntimeException("Invoice not found"));
            inv.setAmountPaid(inv.getAmountPaid().subtract(payment.getAmountPaid()));
            inv.recomputeStatus();
            invoiceRepo.save(inv);
        }
    }

    private PaymentDTO toDTO(Payment entity) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(entity.getPaymentId());
        dto.setInvoiceId(entity.getInvoiceId());
        dto.setStudentId(entity.getStudentId());
        dto.setAmountPaid(entity.getAmountPaid());
        dto.setPaymentMode(entity.getPaymentMode().name());
        dto.setTransactionId(entity.getTransactionId());
        dto.setGatewayReferenceId(entity.getGatewayReferenceId());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }

}
