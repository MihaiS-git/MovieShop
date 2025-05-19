package com.movieshop.server.service;

import com.movieshop.server.domain.Country;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.CountryMapper;
import com.movieshop.server.model.CountryDTO;
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
    public List<CountryDTO> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream().map(countryMapper::toDto).toList();
    }

    @Override
    public CountryDTO getCountryById(Integer id) {
        Country country = getCountryByIdOrElseThrow(id);
        return countryMapper.toDto(country);
    }

    @Override
    public CountryDTO getCountryByName(String name) {
        Country country = countryRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Country not found with name: " + name));

        return countryMapper.toDto(country);
    }

    @Override
    public CountryDTO createCountry(CountryDTO countryDTO) {
        Country savedCountry = countryRepository.save(countryMapper.toEntity(countryDTO));
        return countryMapper.toDto(savedCountry);
    }

    @Override
    public CountryDTO updateCountry(Integer id, CountryDTO countryDTO) {
        Country existentCountry = getCountryByIdOrElseThrow(id);

        existentCountry.setName(countryDTO.getName());

        Country updatedCountry = countryRepository.save(existentCountry);

        return countryMapper.toDto(updatedCountry);
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
