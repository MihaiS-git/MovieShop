package com.movieshop.server.model;

import com.movieshop.server.domain.Inventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponseDTO {

    private Integer id;

    private Integer managerStaffId;

    private Integer addressId;

    private OffsetDateTime lastUpdate;

}
