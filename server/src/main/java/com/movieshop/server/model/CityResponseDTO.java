package com.movieshop.server.model;

import com.movieshop.server.domain.Country;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CityResponseDTO {

    private Integer id;

    private String name;

    private CountryResponseDTO country;

    private OffsetDateTime lastUpdate;
}
