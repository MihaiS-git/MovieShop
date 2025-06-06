package com.movieshop.server.repository;

import com.movieshop.server.domain.City;
import com.movieshop.server.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByName(String name);

    Optional<City> findByNameAndCountry(String name, Country country);
}
