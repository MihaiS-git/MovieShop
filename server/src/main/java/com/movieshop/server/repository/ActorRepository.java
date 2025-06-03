package com.movieshop.server.repository;

import com.movieshop.server.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer> {

    @Query("SELECT a FROM Actor a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :searchName, '%')) OR " +
            "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :searchName, '%')) OR " +
            "LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE LOWER(CONCAT('%', :searchName, '%')) OR " +
            "LOWER(CONCAT(a.lastName, ' ', a.firstName)) LIKE LOWER(CONCAT('%', :searchName, '%'))")
    List<Actor> searchByName(@Param("searchName") String searchName);

}
