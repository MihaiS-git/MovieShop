package com.movieshop.server.controller;

import com.movieshop.server.model.CityRequestDTO;
import com.movieshop.server.model.CityResponseDTO;
import com.movieshop.server.service.ICityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/cities")
public class CityController {

    private final ICityService cityService;

    public CityController(ICityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<CityResponseDTO>> getAllCities() {
        List<CityResponseDTO> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponseDTO> getCityById(@PathVariable Integer id) {
        CityResponseDTO city = cityService.getCityById(id);
        return ResponseEntity.ok(city);
    }

    @PostMapping
    public ResponseEntity<CityResponseDTO> createCity(@RequestBody CityRequestDTO cityRequestDTO) {
        CityResponseDTO createdCity = cityService.createCity(cityRequestDTO);
        return ResponseEntity.status(201).body(createdCity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponseDTO> updateCity(@PathVariable Integer id, @RequestBody CityRequestDTO cityRequestDTO) {
        CityResponseDTO updatedCity = cityService.updateCity(id, cityRequestDTO);
        return ResponseEntity.ok(updatedCity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}
