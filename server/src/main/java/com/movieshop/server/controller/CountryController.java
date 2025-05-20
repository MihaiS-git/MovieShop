package com.movieshop.server.controller;

import com.movieshop.server.model.CountryRequestDTO;
import com.movieshop.server.model.CountryResponseDTO;
import com.movieshop.server.service.ICountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v0/countries")
public class CountryController {

    private final ICountryService countryService;

    public CountryController(ICountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<List<CountryResponseDTO>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryResponseDTO> getCountryById(@PathVariable Integer id) {
        return ResponseEntity.ok(countryService.getCountryById(id));
    }

    @PostMapping
    public ResponseEntity<CountryResponseDTO> createCountry(@RequestBody CountryRequestDTO countryRequestDTO) {
        CountryResponseDTO createdCountry = countryService.createCountry(countryRequestDTO);
        URI location = URI.create("/api/v0/countries/" + createdCountry.getId());
        return ResponseEntity.created(location).body(createdCountry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryResponseDTO> updateCountry(@PathVariable Integer id, @RequestBody CountryRequestDTO countryRequestDTO) {
        return ResponseEntity.ok(countryService.updateCountry(id, countryRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Integer id) {
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }
}
