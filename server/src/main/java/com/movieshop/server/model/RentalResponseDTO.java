package com.movieshop.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalResponseDTO {
    private Integer id;

    private OffsetDateTime rentalDate;

    private Integer inventoryId;

    private Integer customerId;

    private OffsetDateTime returnDate;

    private Integer staffId;

    private OffsetDateTime lastUpdate;

    private Integer rentalPeriod;

    private List<Integer> paymentIds;
}
