package com.movieshop.server.repository;

import com.movieshop.server.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Integer> {

    Optional<Language> findByNameIgnoreCase(String name);

}
