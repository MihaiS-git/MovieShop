package com.movieshop.server.service;

import com.movieshop.server.domain.City;
import com.movieshop.server.model.CityDTO;

import java.util.List;

public interface ICityService {
    List<City> getAllCities();
    City getCityById(Integer id);
    City getCityByName(String name);
    City createCity(CityDTO cityDTO);
    City updateCity(Integer id, CityDTO cityDTO);
    void deleteCity(Integer id);
}
