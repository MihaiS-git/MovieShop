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
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Country getCountryById(Integer id) {
        return getCountryByIdOrElseThrow(id);
    }

    @Override
    public Country getCountryByName(String name) {
        return countryRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Country not found with name: " + name));
    }

    @Override
    public Country createCountry(CountryDTO countryDTO) {
        return countryRepository.save(countryMapper.toEntity(countryDTO));
    }

    @Override
    public Country updateCountry(Integer id, CountryDTO countryDTO) {
        Country existentCountry = getCountryByIdOrElseThrow(id);

        existentCountry.setName(countryDTO.getName());

        return countryRepository.save(existentCountry);
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
