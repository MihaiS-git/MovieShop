package com.movieshop.server.service;

import com.movieshop.server.domain.City;
import com.movieshop.server.model.CityRequestDTO;
import com.movieshop.server.model.CityResponseDTO;

import java.util.List;

public interface ICityService {
    List<CityResponseDTO> getAllCities();
    CityResponseDTO getCityById(Integer id);
    CityResponseDTO createCity(CityRequestDTO cityRequestDTO);
    CityResponseDTO updateCity(Integer id, CityRequestDTO cityRequestDTO);
    void deleteCity(Integer id);
}
