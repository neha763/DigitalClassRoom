package com.digital.servicei;

import com.digital.dto.InvoiceDTO;

import java.util.List;

public interface InvoiceService {
    InvoiceDTO generateInvoice(Long studentId, Long feeId, String dueDate);
    InvoiceDTO getInvoice(Long invoiceId);
    List<InvoiceDTO> getInvoicesByStudent(Long studentId);
    InvoiceDTO applyAdjustment(Long invoiceId, double adjustmentAmount); // discount/fine

    InvoiceDTO updateInvoice(Long id, Long studentId, String dueDate);

    void deleteInvoice(Long id);
}
