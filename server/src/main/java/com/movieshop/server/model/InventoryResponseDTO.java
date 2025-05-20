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
public class InventoryResponseDTO {

    private Integer id;

    private Integer filmId;

    private Integer storeId;

    private OffsetDateTime lastUpdate;

    private List<Integer> rentalIds;

}
