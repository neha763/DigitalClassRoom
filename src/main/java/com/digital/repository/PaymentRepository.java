package com.digital.repository;

import com.digital.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStudentId(Long studentId);
    List<Payment> findByInvoiceId(Long invoiceId);
    Optional<Payment> findByTransactionId(String transactionId);
}
