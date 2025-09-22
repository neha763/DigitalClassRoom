package com.digital.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceDTO {

    private Long invoiceId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Fee ID is required")
    private Long feeId;

    @NotNull(message = "Total due is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Total due cannot be negative")
    private BigDecimal totalDue;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @Pattern(
            regexp = "PAID|PARTIALLY_PAID|UNPAID|OVERDUE",
            message = "Status must be one of: PAID, PARTIALLY_PAID, UNPAID, OVERDUE"
    )
    private String status; // PAID, PARTIALLY_PAID, UNPAID, OVERDUE

    @DecimalMin(value = "0.00", inclusive = true, message = "Amount paid cannot be negative")
    private BigDecimal amountPaid = BigDecimal.ZERO;

    // Getters and Setters
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getFeeId() { return feeId; }
    public void setFeeId(Long feeId) { this.feeId = feeId; }

    public BigDecimal getTotalDue() { return totalDue; }
    public void setTotalDue(BigDecimal totalDue) { this.totalDue = totalDue; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
}
