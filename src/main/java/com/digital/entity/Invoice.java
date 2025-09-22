package com.digital.entity;

import com.digital.enums.InvoiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;

@Entity
@Table(name = "invoices",
        indexes = {@Index(columnList = "student_id"), @Index(columnList = "fee_id")})
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @NotNull
    @Column(name = "student_id", nullable = false)
    private Long studentId; // FK to Student

    @NotNull
    @Column(name = "fee_id", nullable = false)
    private Long feeId; // FK to FeeStructure

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "total_due", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalDue = BigDecimal.ZERO;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private InvoiceStatus status = InvoiceStatus.UNPAID;

    @PastOrPresent
    @Column(name = "generated_at", nullable = false, updatable = false)
    private Instant generatedAt;

    @PastOrPresent
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "amount_paid", nullable = false, precision = 14, scale = 2)
    private BigDecimal amountPaid = BigDecimal.ZERO;

    public Invoice() {}

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.generatedAt = now;
        this.updatedAt = now;
        if (this.status == null) this.status = InvoiceStatus.UNPAID;
        if (this.amountPaid == null) this.amountPaid = BigDecimal.ZERO;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }


    public void recomputeStatus() {
        if (amountPaid == null) amountPaid = BigDecimal.ZERO;
        if (totalDue == null) totalDue = BigDecimal.ZERO;

        int cmp = amountPaid.compareTo(totalDue);
        if (cmp >= 0) {
            this.status = InvoiceStatus.PAID;
            this.amountPaid = totalDue;
        } else if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
            this.status = InvoiceStatus.PARTIALLY_PAID;
        } else {
            if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
                this.status = InvoiceStatus.OVERDUE;
            } else {
                this.status = InvoiceStatus.UNPAID;
            }
        }
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

    public Long getFeeId() {
        return feeId;
    }

    public void setFeeId(Long feeId) {
        this.feeId = feeId;
    }

    public BigDecimal getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(BigDecimal totalDue) {
        this.totalDue = totalDue;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
}
