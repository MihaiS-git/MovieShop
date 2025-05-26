package com.movieshop.server.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequestDTO {

    @NotNull
    @Positive
    private Integer customerId;

    @NotNull
    @Positive
    private Integer staffId;

    @NotNull
    @Positive
    private Integer rentalId;

    @NotNull
    private Double amount;
}
