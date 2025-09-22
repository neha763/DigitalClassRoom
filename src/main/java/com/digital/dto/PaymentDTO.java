package com.digital.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentDTO {

    private Long paymentId;

    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Amount paid is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be at least 0.01")
    private BigDecimal amountPaid;

    @NotBlank(message = "Payment mode is required")
    @Pattern(
            regexp = "CASH|CARD|UPI|PAYMENT_GATEWAY",
            message = "Payment mode must be one of: CASH, CARD, UPI, PAYMENT_GATEWAY"
    )
    private String paymentMode; // CASH, CARD, UPI, PAYMENT_GATEWAY

    @Size(max = 100, message = "Transaction ID cannot exceed 100 characters")
    private String transactionId; // generated automatically for responses

    @Size(max = 100, message = "Gateway reference ID cannot exceed 100 characters")
    private String gatewayReferenceId; // for gateway payments

    @NotBlank(message = "Payment status is required")
    @Pattern(
            regexp = "SUCCESS|FAILED|PENDING|REFUNDED",
            message = "Status must be one of: SUCCESS, FAILED, PENDING, REFUNDED"
    )
    private String status; // SUCCESS, FAILED, PENDING, REFUNDED

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes; // optional notes about payment
}
