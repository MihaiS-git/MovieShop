package com.movieshop.server.service;

import com.movieshop.server.model.RentalRequestDTO;
import com.movieshop.server.model.RentalResponseDTO;

import java.util.List;

public interface IRentalService {
    RentalResponseDTO createRental(RentalRequestDTO rentalRequestDTO);

    RentalResponseDTO getRentalById(Integer rentalId);

    List<RentalResponseDTO> getAllRentals();

    RentalResponseDTO updateRental(Integer rentalId, RentalRequestDTO rentalRequestDTO);

    void deleteRental(Integer rentalId);
}
