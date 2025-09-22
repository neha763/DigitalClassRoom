package com.digital.repository;

import com.digital.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByStudentId(Long studentId);
    List<Invoice> findByStatus(String status); // optional for admin reports
}
