package com.movieshop.server.controller;

import com.movieshop.server.model.RentalRequestDTO;
import com.movieshop.server.model.RentalResponseDTO;
import com.movieshop.server.service.IRentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/rentals")
public class RentalController {

    private final IRentalService rentalService;

    public RentalController(IRentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<RentalResponseDTO>> getAllRentals() {
        List<RentalResponseDTO> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponseDTO> getRentalById(@PathVariable Integer id) {
        RentalResponseDTO rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    @PostMapping
    public ResponseEntity<RentalResponseDTO> createRental(@RequestBody RentalRequestDTO rentalRequestDTO) {
        RentalResponseDTO createdRental = rentalService.createRental(rentalRequestDTO);
        return ResponseEntity.status(201).body(createdRental);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentalResponseDTO> updateRental(@PathVariable Integer id, @RequestBody RentalRequestDTO rentalRequestDTO) {
        RentalResponseDTO updatedRental = rentalService.updateRental(id, rentalRequestDTO);
        return ResponseEntity.ok(updatedRental);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Integer id) {
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }
}
