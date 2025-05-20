package com.movieshop.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreRequestDTO {

    private Integer managerStaffId;

    private Integer addressId;

}
