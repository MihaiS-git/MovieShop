package com.movieshop.server.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryRequestDTO {

    @NotNull(message = "filmId must be provided")
    private Integer filmId;

    @NotNull(message = "storeId must be provided")
    private Integer storeId;

}
