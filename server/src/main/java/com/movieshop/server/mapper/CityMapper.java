package com.movieshop.server.mapper;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.City;
import com.movieshop.server.domain.Country;
import com.movieshop.server.model.CityDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class CityMapper {

    public CityDTO toDto(City city) {
        if (city == null) {
            return null;
        }
        return CityDTO.builder()
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

    public City toEntity(CityDTO cityDTO, Country country, Set<Address> addresses) {
        if (cityDTO == null) {
            return null;
        }
        City city = new City();
        city.setName(cityDTO.getName());
        city.setCountry(country);
        city.setAddresses(addresses != null ? addresses : Collections.emptySet());
        return city;
    }
}
