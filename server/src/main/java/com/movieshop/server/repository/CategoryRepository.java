package com.movieshop.server.repository;

import com.movieshop.server.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

    Optional<Category> findByNameIgnoreCase(String name);

}
