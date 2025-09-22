package com.digital.entity;

import com.digital.enums.PaymentMode;
import com.digital.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments",
        indexes = {@Index(columnList = "invoice_id"), @Index(columnList = "student_id")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"transaction_id"})})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @NotNull
    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId; // FK to Invoice

    @NotNull
    @Column(name = "student_id", nullable = false)
    private Long studentId; // FK to Student

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true) // zero-value payments not allowed
    @Column(name = "amount_paid", nullable = false, precision = 14, scale = 2)
    private BigDecimal amountPaid;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false, length = 30)
    private PaymentMode paymentMode;

    @NotNull
    @Size(max = 100)
    @Column(name = "transaction_id", nullable = false, length = 100, unique = true)
    private String transactionId;

    @Size(max = 100)
    @Column(name = "gateway_reference_id", length = 100)
    private String gatewayReferenceId; // from Mock Gateway

    @PastOrPresent
    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "notes", length = 500)
    private String notes;

    public Payment() {}

    @PrePersist
    public void prePersist() {
        if (this.transactionId == null || this.transactionId.trim().isEmpty()) {
            // generate UUID transaction id by default; override if you want a custom txn
            this.transactionId = "txn_" + UUID.randomUUID().toString();
        }
        if (this.paymentDate == null) {
            this.paymentDate = Instant.now();
        }
        if (this.status == null) {
            this.status = PaymentStatus.PENDING;
        }
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getGatewayReferenceId() {
        return gatewayReferenceId;
    }

    public void setGatewayReferenceId(String gatewayReferenceId) {
        this.gatewayReferenceId = gatewayReferenceId;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
