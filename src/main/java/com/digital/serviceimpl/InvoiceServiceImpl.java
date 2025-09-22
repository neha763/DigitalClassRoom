package com.digital.serviceimpl;

import com.digital.dto.InvoiceDTO;
import com.digital.entity.FeeStructure;
import com.digital.entity.Invoice;
import com.digital.enums.InvoiceStatus;
import com.digital.repository.FeeStructureRepository;
import com.digital.repository.InvoiceRepository;
import com.digital.servicei.InvoiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepo;
    private final FeeStructureRepository feeRepo;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepo, FeeStructureRepository feeRepo) {
        this.invoiceRepo = invoiceRepo;
        this.feeRepo = feeRepo;
    }

    @Override
    public InvoiceDTO generateInvoice(Long studentId, Long feeId, String dueDateStr) {
        FeeStructure fs = feeRepo.findById(feeId)
                .orElseThrow(() -> new RuntimeException("Fee structure not found"));

        Invoice invoice = new Invoice();
        invoice.setStudentId(studentId);
        invoice.setFeeId(fs.getFeeId());
        invoice.setTotalDue(fs.getTotalAmount());
        invoice.setDueDate(LocalDate.parse(dueDateStr));
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setAmountPaid(BigDecimal.ZERO);

        Invoice saved = invoiceRepo.save(invoice);
        return toDTO(saved);
    }

    @Override
    public InvoiceDTO getInvoice(Long invoiceId) {
        return invoiceRepo.findById(invoiceId)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    @Override
    public List<InvoiceDTO> getInvoicesByStudent(Long studentId) {
        return invoiceRepo.findByStudentId(studentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO applyAdjustment(Long invoiceId, double adjustmentAmount) {
        Invoice inv = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        BigDecimal adj = BigDecimal.valueOf(adjustmentAmount);
        inv.setTotalDue(inv.getTotalDue().add(adj));
        inv.recomputeStatus();

        return toDTO(invoiceRepo.save(inv));
    }

    @Override
    public InvoiceDTO updateInvoice(Long id, Long studentId, String dueDateStr) {
        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (studentId != null) {
            invoice.setStudentId(studentId);
        }

        if (dueDateStr != null) {
            invoice.setDueDate(LocalDate.parse(dueDateStr));
        }

        invoice.recomputeStatus();
        return toDTO(invoiceRepo.save(invoice));
    }

    @Override
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoiceRepo.delete(invoice);
    }

    private InvoiceDTO toDTO(Invoice inv) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceId(inv.getInvoiceId());
        dto.setStudentId(inv.getStudentId());
        dto.setFeeId(inv.getFeeId());
        dto.setTotalDue(inv.getTotalDue());
        dto.setDueDate(inv.getDueDate());
        dto.setStatus(inv.getStatus().name());
        dto.setAmountPaid(inv.getAmountPaid());
        return dto;
    }
}
