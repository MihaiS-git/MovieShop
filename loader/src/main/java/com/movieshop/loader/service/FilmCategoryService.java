package com.movieshop.loader.service;

import com.movieshop.loader.domain.Category;
import com.movieshop.loader.domain.Film;
import com.movieshop.loader.repository.CategoryRepository;
import com.movieshop.loader.repository.FilmRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmCategoryService {

    private final FilmRepository filmRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void loadFilmCategoryData() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/film_category.csv")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("CSV file not found on classpath: data/film_category.com.movieshop.loader.csv");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line = reader.readLine();

                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");

                    int filmId = Integer.parseInt(fields[0]);
                    int categoryId = Integer.parseInt(fields[1]);

                    Film film = filmRepository.findById(filmId).orElse(null);
                    Category category = categoryRepository.findById(categoryId).orElse(null);

                    if (film != null && category != null) {
                        if (!film.getCategories().contains(category)) {
                            film.addCategory(category);
                        }
                    }
                }
                filmRepository.flush();
            }
        } catch (Exception e) {
            log.error("Failed to load film-category data", e);
            throw new RuntimeException("Failed to load film-category data", e);
        }
    }
}
