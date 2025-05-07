package com.movieshop.server.repository;

import com.movieshop.server.domain.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FilmRepository extends JpaRepository<Film, Long> {
    Page<Film> findAll(Pageable pageable);

}
