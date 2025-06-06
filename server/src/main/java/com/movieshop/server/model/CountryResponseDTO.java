package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CountryResponseDTO {

    private Integer id;

    private String name;

    private OffsetDateTime lastUpdate;
}
