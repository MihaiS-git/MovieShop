package com.movieshop.server.service;

import com.movieshop.server.domain.City;
import com.movieshop.server.domain.Country;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.CityMapper;
import com.movieshop.server.model.CityRequestDTO;
import com.movieshop.server.model.CityResponseDTO;
import com.movieshop.server.repository.CityRepository;
import com.movieshop.server.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements ICityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final CountryRepository countryRepository;

    public CityServiceImpl(
            CityRepository cityRepository,
            CityMapper cityMapper,
            CountryRepository countryRepository
    ) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
        this.countryRepository = countryRepository;
    }

    @Override
    public List<CityResponseDTO> getAllCities() {
        List<City> cities = cityRepository.findAll();
        return cities.stream()
                .map(cityMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CityResponseDTO getCityById(Integer id) {
        City city = getCityByIdOrElseThrow(id);
        return cityMapper.toResponseDto(city);
    }

    @Override
    public CityResponseDTO createCity(CityRequestDTO cityRequestDTO) {
        Country country = countryRepository.findByName(cityRequestDTO.getCountry()).orElseThrow(() ->
                new ResourceNotFoundException("Country not found with name: " + cityRequestDTO.getCountry()));

        City city = cityMapper.toEntity(cityRequestDTO);
        country.addCity(city);

        city = cityRepository.save(city);
        return cityMapper.toResponseDto(city);
    }

    @Override
    public CityResponseDTO updateCity(Integer id, CityRequestDTO cityRequestDTO) {
        City existentCity = getCityByIdOrElseThrow(id);

        Country country = countryRepository.findByName(cityRequestDTO.getCountry()).orElseThrow(() ->
                new ResourceNotFoundException("Country not found with name: " + cityRequestDTO.getCountry()));

        existentCity.setName(cityRequestDTO.getName());
        country.addCity(existentCity);

        existentCity = cityRepository.save(existentCity);

        return cityMapper.toResponseDto(existentCity);
    }

    @Override
    public void deleteCity(Integer id) {
        City existentCity = getCityByIdOrElseThrow(id);
        cityRepository.deleteById(id);
    }

    private City getCityByIdOrElseThrow(Integer id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
    }
}
