package com.movieshop.server.repository;

import com.movieshop.server.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
}
