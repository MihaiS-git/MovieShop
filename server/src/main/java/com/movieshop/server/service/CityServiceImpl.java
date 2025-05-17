package com.movieshop.server.service;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.City;
import com.movieshop.server.domain.Country;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.CityMapper;
import com.movieshop.server.model.CityDTO;
import com.movieshop.server.repository.AddressRepository;
import com.movieshop.server.repository.CityRepository;
import com.movieshop.server.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements ICityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;

    public CityServiceImpl(
            CityRepository cityRepository,
            CityMapper cityMapper,
            AddressRepository addressRepository,
            CountryRepository countryRepository
    ) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
        this.addressRepository = addressRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public City getCityById(Integer id) {
        return getCityByIdOrElseThrow(id);
    }

    @Override
    public City getCityByName(String name) {
        return cityRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with name: " + name));
    }

    @Override
    public City createCity(CityDTO cityDTO) {
        List<Integer> addressIds = cityDTO.getAddressIds();
        Set<Address> addresses = !addressIds.isEmpty()
                ? addressIds.stream().map(id -> addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id))).collect(Collectors.toSet())
                : new HashSet<>();
        Country country = countryRepository.findByName(cityDTO.getCountry()).orElseThrow(() ->
                new ResourceNotFoundException("Country not found with name: " + cityDTO.getCountry()));

        City city = cityMapper.toEntity(cityDTO, country, addresses);

        return cityRepository.save(city);
    }

    @Override
    public City updateCity(Integer id, CityDTO cityDTO) {
        City existentCity = getCityByIdOrElseThrow(id);

        Country country = countryRepository.findByName(cityDTO.getCountry()).orElseThrow(() ->
                new ResourceNotFoundException("Country not found with name: " + cityDTO.getCountry()));

        existentCity.setName(cityDTO.getName());
        existentCity.setCountry(country);

        return cityRepository.save(existentCity);
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
