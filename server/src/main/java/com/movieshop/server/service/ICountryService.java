package com.movieshop.server.service;

import com.movieshop.server.domain.Country;
import com.movieshop.server.model.CountryDTO;

import java.util.List;

public interface ICountryService {
    List<Country> getAllCountries();
    Country getCountryById(Integer id);
    Country getCountryByName(String name);
    Country createCountry(CountryDTO countryDTO);
    Country updateCountry(Integer id, CountryDTO countryDTO);
    void deleteCountry(Integer id);
}
