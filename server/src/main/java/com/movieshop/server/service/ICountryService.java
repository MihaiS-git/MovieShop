package com.movieshop.server.service;

import com.movieshop.server.model.CountryRequestDTO;
import com.movieshop.server.model.CountryResponseDTO;

import java.util.List;

public interface ICountryService {
    List<CountryResponseDTO> getAllCountries();
    CountryResponseDTO getCountryById(Integer id);
    CountryResponseDTO getCountryByName(String name);
    CountryResponseDTO createCountry(CountryRequestDTO countryDTO);
    CountryResponseDTO updateCountry(Integer id, CountryRequestDTO countryDTO);
    void deleteCountry(Integer id);
}
