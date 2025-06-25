package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryItemDTO {

    private Integer id;

    private Integer storeId;

    private MovieInventoryItemDTO film;

    private OffsetDateTime lastUpdate;

}
