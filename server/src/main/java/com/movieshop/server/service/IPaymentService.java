package com.movieshop.server.service;

import com.movieshop.server.model.PaymentRequestDTO;
import com.movieshop.server.model.PaymentResponseDTO;

import java.util.List;

public interface IPaymentService {
    List<PaymentResponseDTO> getAllPayments();
    PaymentResponseDTO getPaymentById(Integer paymentId);
    PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO updatePayment(Integer paymentId, PaymentRequestDTO paymentRequestDTO);
    void deletePayment(Integer paymentId);
}
