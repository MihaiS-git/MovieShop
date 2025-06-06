package com.movieshop.server.mapper;

import com.movieshop.server.domain.City;
import com.movieshop.server.model.CityRequestDTO;
import com.movieshop.server.model.CityResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CityMapper {

    public final CountryMapper countryMapper;

    public CityResponseDTO toResponseDto(City city) {
        if (city == null) {
            return null;
        }
        return CityResponseDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .country(countryMapper.toResponseDto(city.getCountry()))
                .lastUpdate(city.getLastUpdate())
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
