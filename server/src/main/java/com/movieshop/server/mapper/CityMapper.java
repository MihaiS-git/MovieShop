package com.movieshop.server.mapper;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.City;
import com.movieshop.server.model.CityRequestDTO;
import com.movieshop.server.model.CityResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CityMapper {

    public CityResponseDTO toResponseDto(City city) {
        if (city == null) {
            return null;
        }
        return CityResponseDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .country(city.getCountry().getName())
                .addressIds(city.getAddresses() != null
                        ? (city.getAddresses().stream()
                        .map(Address::getId)
                        .toList())
                        : Collections.emptyList())
                .build();
    }

    public City toEntity(CityRequestDTO cityRequestDTO) {
        if (cityRequestDTO == null) {
            return null;
        }
        City city = new City();
        city.setName(cityRequestDTO.getName());
        return city;
    }
}
