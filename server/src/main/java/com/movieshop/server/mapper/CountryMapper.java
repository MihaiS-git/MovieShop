package com.movieshop.server.mapper;

import com.movieshop.server.domain.City;
import com.movieshop.server.domain.Country;
import com.movieshop.server.model.CountryDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class CountryMapper {

    public CountryDTO toDTO(Country country) {
        if (country == null) {
            return null;
        }
        return CountryDTO.builder()
                .id(country.getId())
                .name(country.getName())
                .cities(country.getCities() != null
                        ? (country.getCities().stream()
                        .map(City::getId)
                        .toList())
                        : Collections.emptyList())
                .build();
    }

    public Country toEntity(CountryDTO countryDTO, Set<City> cities) {
        if (countryDTO == null) {
            return null;
        }

        Country country = new Country();
        country.setName(countryDTO.getName());
        country.setCities(cities != null ? cities : Collections.emptySet());
        return country;
    }
}
