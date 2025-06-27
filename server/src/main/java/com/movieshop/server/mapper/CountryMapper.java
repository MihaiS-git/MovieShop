package com.movieshop.server.mapper;

import com.movieshop.server.domain.Country;
import com.movieshop.server.model.AddressProjection;
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
                .lastUpdate(country.getLastUpdate())
                .build();
    }

    public CountryResponseDTO projectionToResponseDto(AddressProjection.CityInfo.CountryInfo country) {
        if (country == null) {
            return null;
        }
        return CountryResponseDTO.builder()
                .id(country.getId())
                .name(country.getName())
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
