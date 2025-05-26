package com.movieshop.server.mapper;

import com.movieshop.server.domain.Payment;
import com.movieshop.server.domain.Rental;
import com.movieshop.server.model.RentalRequestDTO;
import com.movieshop.server.model.RentalResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {
    public RentalResponseDTO toResponseDto(Rental rental){
        if (rental == null) {
            return null;
        }
        return RentalResponseDTO.builder()
                .id(rental.getId())
                .rentalDate(rental.getRentalDate())
                .inventoryId(rental.getInventory().getId())
                .customerId(rental.getCustomer().getId())
                .returnDate(rental.getReturnDate())
                .staffId(rental.getStaff().getId())
                .lastUpdate(rental.getLastUpdate())
                .rentalPeriod(rental.getRentalPeriod())
                .paymentIds(rental.getPayments().stream()
                        .map(Payment::getPaymentId)
                        .toList())
                .build();
    }

    public Rental toEntity(RentalRequestDTO rentalRequestDTO) {
        if (rentalRequestDTO == null) {
            return null;
        }
        Rental rental = new Rental();
        rental.setRentalDate(rentalRequestDTO.getRentalDate());
        rental.setRentalPeriod(rentalRequestDTO.getRentalPeriod());
        return rental;
    }
}
