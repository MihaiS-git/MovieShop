package com.movieshop.server.mapper;

import com.movieshop.server.domain.City;
import com.movieshop.server.domain.Country;
import com.movieshop.server.model.CountryRequestDTO;
import com.movieshop.server.model.CountryResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CountryMapper {

    public CountryResponseDTO toResponseDto(Country country) {
        if (country == null) {
            return null;
        }
        return CountryResponseDTO.builder()
                .id(country.getId())
                .name(country.getName())
                .cityIds(country.getCities() != null
                        ? (country.getCities().stream()
                        .map(City::getId)
                        .toList())
                        : Collections.emptyList())
                .lastUpdate(country.getLastUpdate())
                .build();
    }

    public Country toEntity(CountryRequestDTO countryDTO) {
        if (countryDTO == null) {
            return null;
        }

        Country country = new Country();
        country.setName(countryDTO.getName());
        return country;
    }
}
