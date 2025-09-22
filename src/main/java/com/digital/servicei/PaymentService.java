package com.digital.servicei;

import com.digital.dto.GatewayResponseDTO;
import com.digital.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    PaymentDTO makePayment(PaymentDTO dto);
    List<PaymentDTO> getPaymentsByStudent(Long studentId);
    GatewayResponseDTO processGatewayCallback(GatewayResponseDTO response);

    // 🔹 New method: update payment (e.g., refund, adjust)
    PaymentDTO updatePayment(Long paymentId, PaymentDTO dto);

    // 🔹 New method: delete payment (admin only in real system)
    void deletePayment(Long paymentId);

    //GatewayResponseDTO processGatewayCallback(GatewayResponseDTO response);
}
