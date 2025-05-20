package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CityResponseDTO {

    private Integer id;

    private String name;

    private String country;

    private List<Integer> addressIds;

    private OffsetDateTime lastUpdate;
}
