package com.movieshop.server.bootstrap;

import com.movieshop.server.domain.Category;
import com.movieshop.server.domain.Film;
import com.movieshop.server.repository.CategoryRepository;
import com.movieshop.server.repository.FilmRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmCategoryService {

    private final FilmRepository filmRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void loadFilmCategoryData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/film_category_data_cleaned.csv"))) {
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                Film film = filmRepository.findById(Integer.parseInt(fields[0])).orElse(null);
                Category category = categoryRepository.findById(Integer.parseInt(fields[1])).orElse(null);

                log.info(fields[0] + " " + fields[1]);

                if (film != null && category != null) {
                    film.getCategories().add(category);
                    filmRepository.save(film);
                    category.getFilms().add(film);
                    categoryRepository.save(category);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
