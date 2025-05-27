package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CountryResponseDTO {

    private Integer id;

    private String name;

    private List<Integer> cityIds;

    private OffsetDateTime lastUpdate;
}
