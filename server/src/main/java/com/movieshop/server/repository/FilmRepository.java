package com.movieshop.server.repository;

import com.movieshop.server.domain.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface FilmRepository extends JpaRepository<Film, Integer> {
    Page<Film> findAll(Pageable pageable);
    Optional<Film> findByTitle(String title);
}
