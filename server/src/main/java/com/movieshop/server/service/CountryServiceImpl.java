package com.movieshop.server.service;

import com.movieshop.server.domain.Country;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.CountryMapper;
import com.movieshop.server.model.CountryRequestDTO;
import com.movieshop.server.model.CountryResponseDTO;
import com.movieshop.server.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements ICountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CountryServiceImpl(
            CountryRepository countryRepository,
            CountryMapper countryMapper
    ) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    @Override
    public List<CountryResponseDTO> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream().map(countryMapper::toResponseDto).toList();
    }

    @Override
    public CountryResponseDTO getCountryById(Integer id) {
        Country country = getCountryByIdOrElseThrow(id);
        return countryMapper.toResponseDto(country);
    }

    @Override
    public CountryResponseDTO getCountryByName(String name) {
        Country country = countryRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Country not found with name: " + name));

        return countryMapper.toResponseDto(country);
    }

    @Override
    public CountryResponseDTO createCountry(CountryRequestDTO countryRequestDTO) {
        Country savedCountry = countryRepository.save(countryMapper.toEntity(countryRequestDTO));
        return countryMapper.toResponseDto(savedCountry);
    }

    @Override
    public CountryResponseDTO updateCountry(Integer id, CountryRequestDTO countryRequestDTO) {
        Country existentCountry = getCountryByIdOrElseThrow(id);

        existentCountry.setName(countryRequestDTO.getName());

        Country updatedCountry = countryRepository.save(existentCountry);

        return countryMapper.toResponseDto(updatedCountry);
    }

    @Override
    public void deleteCountry(Integer id) {
        Country existentCountry = getCountryByIdOrElseThrow(id);
        countryRepository.deleteById(id);
    }

    private Country getCountryByIdOrElseThrow(Integer id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));
    }
}
