package com.movieshop.server.repository;

import com.movieshop.server.domain.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FilmRepository extends JpaRepository<Film, Integer>, JpaSpecificationExecutor<Film> {

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

    List<Film> findAllByTitleContainingIgnoreCase(String searchTerm);

}
