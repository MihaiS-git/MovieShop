package com.movieshop.loader.service;

import com.movieshop.loader.csv.RentalCsv;
import com.movieshop.loader.domain.Inventory;
import com.movieshop.loader.domain.Rental;
import com.movieshop.loader.domain.User;
import com.movieshop.loader.repository.InventoryRepository;
import com.movieshop.loader.repository.RentalRepository;
import com.movieshop.loader.repository.UserRepository;
import com.movieshop.loader.utils.CsvReaderUtil;
import com.movieshop.loader.utils.ParsingUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void load() {
        if (rentalRepository.count() > 0) {
            return;
        }

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/rental.csv")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("CSV file not found on classpath: data/rental.csv");
            }

            List<RentalCsv> rentalCsvList = CsvReaderUtil.readCsv(inputStream, RentalCsv.class);

            for (RentalCsv rentalCsv : rentalCsvList) {
                Rental rental = new Rental();

                rental.setRentalDate(OffsetDateTime.parse(rentalCsv.getRentalDate()));

                Inventory inventory = ParsingUtils.safeFindById(
                        ParsingUtils.parseIntSafe(rentalCsv.getInventoryId()),
                        inventoryRepository,
                        "Inventory not found with id: " + rentalCsv.getInventoryId()
                );
                rental.setInventory(inventory);

                User customer = ParsingUtils.safeFindById(
                        ParsingUtils.parseIntSafe(rentalCsv.getCustomerId()),
                        userRepository,
                        "Customer not found with id: " + rentalCsv.getCustomerId()
                );
                rental.setCustomer(customer);

                String returnDateStr = rentalCsv.getReturnDate();
                OffsetDateTime returnDate = (returnDateStr == null || returnDateStr.isBlank())
                        ? OffsetDateTime.now()
                        : OffsetDateTime.parse(returnDateStr);
                rental.setReturnDate(returnDate);

                User staff = ParsingUtils.safeFindById(
                        ParsingUtils.parseIntSafe(rentalCsv.getStaffId()),
                        userRepository,
                        "Staff not found with id: " + rentalCsv.getStaffId()
                );
                rental.setStaff(staff);

                Rental savedRental = rentalRepository.save(rental);

                inventory.addRental(savedRental);
                customer.addRental(savedRental);
                staff.addRental(savedRental);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load rental data", e);
        }
    }
}
