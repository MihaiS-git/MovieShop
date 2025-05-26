package com.movieshop.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Integer paymentId;

    private Integer customerId;

    private Integer staffId;

    private Integer rentalId;

    private Double amount;

    private OffsetDateTime paymentDate;
}
