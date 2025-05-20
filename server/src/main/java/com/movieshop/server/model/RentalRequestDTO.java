package com.movieshop.server.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalRequestDTO {

    private OffsetDateTime rentalDate;

    @NotNull
    @Positive
    private Integer inventoryId;

    @NotNull
    @Positive
    private Integer customerId;

    @NotNull
    @Positive
    private Integer staffId;

    @NotNull
    @Positive
    private Integer rentalPeriod;
}
