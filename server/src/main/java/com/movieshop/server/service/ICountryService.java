package com.movieshop.server.service;

import com.movieshop.server.domain.Country;
import com.movieshop.server.model.CountryDTO;

import java.util.List;

public interface ICountryService {
    List<CountryDTO> getAllCountries();
    CountryDTO getCountryById(Integer id);
    CountryDTO getCountryByName(String name);
    CountryDTO createCountry(CountryDTO countryDTO);
    CountryDTO updateCountry(Integer id, CountryDTO countryDTO);
    void deleteCountry(Integer id);
}
