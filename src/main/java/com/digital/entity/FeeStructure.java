package com.digital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "fee_structures",
        indexes = {@Index(columnList = "class_id, academic_year")})
public class FeeStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_id")
    private Long feeId;

    @NotNull
    @Column(name = "class_id", nullable = false)
    private Long classId; // FK to Class (store id only; use relationship if Class entity exists)

    @NotBlank
    @Size(max = 20)
    @Column(name = "academic_year", nullable = false, length = 20)
    private String academicYear;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "tuition_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal tuitionFee = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "exam_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal examFee = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "transport_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal transportFee = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "library_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal libraryFee = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "other_charges", nullable = false, precision = 12, scale = 2)
    private BigDecimal otherCharges = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @PastOrPresent
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PastOrPresent
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public FeeStructure() {}

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        recomputeTotal();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
        recomputeTotal();
    }

    private void recomputeTotal() {
        this.totalAmount = (safe(tuitionFee)
                .add(safe(examFee))
                .add(safe(transportFee))
                .add(safe(libraryFee))
                .add(safe(otherCharges)));
    }

    private BigDecimal safe(BigDecimal b) {
        return b == null ? BigDecimal.ZERO : b;
    }

    public Long getFeeId() {
        return feeId;
    }

    public void setFeeId(Long feeId) {
        this.feeId = feeId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public BigDecimal getTuitionFee() {
        return tuitionFee;
    }

    public void setTuitionFee(BigDecimal tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    public BigDecimal getExamFee() {
        return examFee;
    }

    public void setExamFee(BigDecimal examFee) {
        this.examFee = examFee;
    }

    public BigDecimal getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(BigDecimal transportFee) {
        this.transportFee = transportFee;
    }

    public BigDecimal getLibraryFee() {
        return libraryFee;
    }

    public void setLibraryFee(BigDecimal libraryFee) {
        this.libraryFee = libraryFee;
    }

    public BigDecimal getOtherCharges() {
        return otherCharges;
    }

    public void setOtherCharges(BigDecimal otherCharges) {
        this.otherCharges = otherCharges;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
