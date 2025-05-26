package com.movieshop.server.service;

import com.movieshop.server.domain.Payment;
import com.movieshop.server.domain.Rental;
import com.movieshop.server.domain.User;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.PaymentMapper;
import com.movieshop.server.model.PaymentRequestDTO;
import com.movieshop.server.model.PaymentResponseDTO;
import com.movieshop.server.repository.PaymentRepository;
import com.movieshop.server.repository.RentalRepository;
import com.movieshop.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements IPaymentService{

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            PaymentMapper paymentMapper,
            UserRepository userRepository,
            RentalRepository rentalRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(paymentMapper::toResponseDto)
                .toList();
    }

    @Override
    public PaymentResponseDTO getPaymentById(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO) {
        Payment payment = paymentMapper.toEntity(paymentRequestDTO);
        User customer = userRepository.findById(paymentRequestDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + paymentRequestDTO.getCustomerId()));
        User staff = userRepository.findById(paymentRequestDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + paymentRequestDTO.getStaffId()));
        Rental rental= rentalRepository.findById(paymentRequestDTO.getRentalId())
                .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + paymentRequestDTO.getRentalId()));

        customer.addCustomerPayment(payment);
        staff.addStaffPayment(payment);
        rental.addPayment(payment);

        payment = paymentRepository.save(payment);

        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public PaymentResponseDTO updatePayment(Integer paymentId, PaymentRequestDTO paymentRequestDTO) {
        Payment existentPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentId));

        existentPayment.setAmount(paymentRequestDTO.getAmount());

        existentPayment = paymentRepository.save(existentPayment);

        return paymentMapper.toResponseDto(existentPayment);
    }

    @Override
    public void deletePayment(Integer paymentId) {
        Payment existentPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentId));
        paymentRepository.delete(existentPayment);
    }
}
