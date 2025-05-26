package com.movieshop.server.repository;

import com.movieshop.server.domain.Film;
import com.movieshop.server.model.FilmListItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface FilmRepository extends JpaRepository<Film, Integer> {

    @Query("SELECT new com.movieshop.server.model.FilmListItemDTO(f.id, f.title, f.description, f.rating) FROM Film f")
    Page<FilmListItemDTO> findAllListItemFilms(Pageable pageable);

    Optional<Film> findByTitle(String title);

    @Query("SELECT f FROM Film f" +
            " LEFT JOIN FETCH f.language" +
            " LEFT JOIN FETCH f.originalLanguage" +
            " LEFT JOIN FETCH f.categories" +
            " LEFT JOIN FETCH f.actors" +
            " LEFT JOIN FETCH f.inventories" +
            " WHERE f.id = :id"
    )
    Optional<Film> findByIdWithAllRelations(@Param("id") Integer id);

    @Query("SELECT f FROM Film f" +
            " LEFT JOIN FETCH f.language" +
            " LEFT JOIN FETCH f.originalLanguage" +
            " WHERE f.id = :id"
    )
    Optional<Film> findByIdWithBasicFields(@Param("id") Integer id);

}
