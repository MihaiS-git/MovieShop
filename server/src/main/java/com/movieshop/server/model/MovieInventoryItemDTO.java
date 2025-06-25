package com.movieshop.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieInventoryItemDTO {
    private Integer id;

    private String title;

    private Integer releaseYear;

    private Double rentalRate;

    private Double replacementCost;
}
