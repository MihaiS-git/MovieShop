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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllPayments_returnsMappedDTOs() {
        Payment payment = new Payment();
        PaymentResponseDTO dto = new PaymentResponseDTO();

        when(paymentRepository.findAll()).thenReturn(List.of(payment));
        when(paymentMapper.toResponseDto(payment)).thenReturn(dto);

        List<PaymentResponseDTO> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
        verify(paymentRepository).findAll();
        verify(paymentMapper).toResponseDto(payment);
    }

    @Test
    void getPaymentById_validId_returnsDTO() {
        Payment payment = new Payment();
        PaymentResponseDTO dto = new PaymentResponseDTO();

        when(paymentRepository.findById(1)).thenReturn(Optional.of(payment));
        when(paymentMapper.toResponseDto(payment)).thenReturn(dto);

        PaymentResponseDTO result = paymentService.getPaymentById(1);

        assertEquals(dto, result);
        verify(paymentRepository).findById(1);
    }

    @Test
    void getPaymentById_invalidId_throwsException() {
        when(paymentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.getPaymentById(999));
    }

    @Test
    void createPayment_validRequest_returnsSavedDTO() {
        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .customerId(1)
                .staffId(2)
                .rentalId(3)
                .amount(10.0)
                .build();

        User customer = mock(User.class);
        User staff = mock(User.class);
        Rental rental = mock(Rental.class);
        Payment payment = new Payment();
        Payment savedPayment = new Payment();
        PaymentResponseDTO responseDTO = new PaymentResponseDTO();

        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(userRepository.findById(1)).thenReturn(Optional.of(customer));
        when(userRepository.findById(2)).thenReturn(Optional.of(staff));
        when(rentalRepository.findById(3)).thenReturn(Optional.of(rental));
        when(paymentRepository.save(payment)).thenReturn(savedPayment);
        when(paymentMapper.toResponseDto(savedPayment)).thenReturn(responseDTO);

        PaymentResponseDTO result = paymentService.createPayment(dto);

        assertEquals(responseDTO, result);
        verify(customer).addCustomerPayment(payment);
        verify(staff).addStaffPayment(payment);
        verify(rental).addPayment(payment);
    }


    @Test
    void createPayment_invalidCustomer_throwsException() {
        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .customerId(1)
                .staffId(2)
                .rentalId(3)
                .amount(10.0)
                .build();

        when(paymentMapper.toEntity(dto)).thenReturn(new Payment());
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.createPayment(dto));
    }

    @Test
    void updatePayment_validId_updatesAmount() {
        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .amount(20.0)
                .build();

        Payment existing = new Payment();
        existing.setAmount(10.0);

        when(paymentRepository.findById(1)).thenReturn(Optional.of(existing));
        when(paymentRepository.save(existing)).thenReturn(existing);
        when(paymentMapper.toResponseDto(existing)).thenReturn(new PaymentResponseDTO());

        PaymentResponseDTO result = paymentService.updatePayment(1, dto);

        assertEquals(20.0, existing.getAmount());
        verify(paymentRepository).save(existing);
    }

    @Test
    void updatePayment_invalidId_throwsException() {
        when(paymentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.updatePayment(1, new PaymentRequestDTO()));
    }

    @Test
    void deletePayment_validId_deletes() {
        Payment payment = new Payment();
        when(paymentRepository.findById(1)).thenReturn(Optional.of(payment));

        paymentService.deletePayment(1);

        verify(paymentRepository).delete(payment);
    }

    @Test
    void deletePayment_invalidId_throwsException() {
        when(paymentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.deletePayment(999));
    }
}
