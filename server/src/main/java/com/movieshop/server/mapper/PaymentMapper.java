package com.movieshop.server.mapper;

import com.movieshop.server.domain.Payment;
import com.movieshop.server.model.PaymentRequestDTO;
import com.movieshop.server.model.PaymentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentResponseDTO toResponseDto(Payment payment) {
        return PaymentResponseDTO.builder()
                .paymentId(payment.getPaymentId())
                .customerId(payment.getCustomer().getId())
                .staffId(payment.getStaff().getId())
                .rentalId(payment.getRental().getId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .build();
    }

    public Payment toEntity(PaymentRequestDTO paymentRequestDTO) {
        Payment payment = new Payment();
        payment.setAmount(paymentRequestDTO.getAmount());

        return payment;
    }
}
