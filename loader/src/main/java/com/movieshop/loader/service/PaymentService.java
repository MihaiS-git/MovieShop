package com.movieshop.loader.service;

import com.movieshop.loader.csv.PaymentCsv;
import com.movieshop.loader.utils.CsvReaderUtil;
import com.movieshop.loader.utils.ParsingUtils;
import com.movieshop.loader.domain.Payment;
import com.movieshop.loader.domain.Rental;
import com.movieshop.loader.domain.User;
import com.movieshop.loader.repository.PaymentRepository;
import com.movieshop.loader.repository.RentalRepository;
import com.movieshop.loader.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 500;

    @Transactional
    public void load() {
        if (paymentRepository.count() > 0) {
            log.info("Payments already loaded. Skipping...");
            return;
        }
        System.out.println("==> loadPayments() CALLED");
        log.info("Starting to load payments...");

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/payment_data_cleaned.csv")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("CSV file not found: data/payment_data_cleaned.csv");
            }

            List<PaymentCsv> paymentCsvList = CsvReaderUtil.readCsv(inputStream, PaymentCsv.class);
            log.info("Found {} payment rows to process.", paymentCsvList.size());

            int successCount = 0;
            int failCount = 0;

            for (int i = 0; i < paymentCsvList.size(); i++) {
                try {
                    PaymentCsv paymentCsv = paymentCsvList.get(i);
                    Payment payment = new Payment();

                    payment.setPaymentId(ParsingUtils.parseIntSafe(paymentCsv.getPaymentId()));

                    User customer = ParsingUtils.safeFindById(
                            ParsingUtils.parseIntSafe(paymentCsv.getCustomerId()),
                            userRepository,
                            "Customer not found with id: " + paymentCsv.getCustomerId()
                    );
                    payment.setCustomer(customer);

                    User staff = ParsingUtils.safeFindById(
                            ParsingUtils.parseIntSafe(paymentCsv.getStaffId()),
                            userRepository,
                            "Staff not found with id: " + paymentCsv.getStaffId()
                    );
                    payment.setStaff(staff);

                    Rental rental = ParsingUtils.safeFindById(
                            ParsingUtils.parseIntSafe(paymentCsv.getRentalId()),
                            rentalRepository,
                            "Rental not found with id: " + paymentCsv.getRentalId()
                    );
                    payment.setRental(rental);

                    payment.setAmount(ParsingUtils.parseDoubleSafe(paymentCsv.getAmount()));
                    payment.setPaymentDate(OffsetDateTime.parse(paymentCsv.getPaymentDate()));

                    entityManager.persist(payment);

                    // Optional: update relations
                    staff.addStaffPayment(payment);
                    rental.addPayment(payment);

                    successCount++;

                    if (i > 0 && i % BATCH_SIZE == 0) {
                        entityManager.flush();
                        entityManager.clear();
                        log.info("Flushed and cleared EntityManager after {} inserts", i);
                    }

                } catch (Exception e) {
                    log.error("Failed row {} | paymentId: {}, customerId: {}, staffId: {}, rentalId: {} | Reason: {}",
                            i,
                            paymentCsvList.get(i).getPaymentId(),
                            paymentCsvList.get(i).getCustomerId(),
                            paymentCsvList.get(i).getStaffId(),
                            paymentCsvList.get(i).getRentalId(),
                            e.getMessage(),
                            e
                    );
                    failCount++;
                }
            }

            // Final flush after loop ends
            entityManager.flush();
            entityManager.clear();

            log.info("Finished loading payments. Success: {}, Failed: {}", successCount, failCount);

        } catch (Exception e) {
            log.error("Unexpected error while loading payment CSV: {}", e.getMessage(), e);
        }
    }
}