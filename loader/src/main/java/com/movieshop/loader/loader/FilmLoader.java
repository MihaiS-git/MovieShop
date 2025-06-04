package com.movieshop.loader.loader;

import com.movieshop.loader.csv.FilmCsv;
import com.movieshop.loader.utils.CsvReaderUtil;
import com.movieshop.loader.utils.ParsingUtils;
import com.movieshop.loader.domain.Film;
import com.movieshop.loader.domain.Rating;
import com.movieshop.loader.repository.FilmRepository;
import com.movieshop.loader.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmLoader {

    private final FilmRepository filmRepository;
    private final LanguageRepository languageRepository;

    public void load() {
        if (filmRepository.count() > 0) {
            return;
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/film.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV file not found on classpath: data/film.com.movieshop.loader.csv");
        }
        List<FilmCsv> filmCsvList = CsvReaderUtil.readCsv(inputStream, FilmCsv.class);

        List<Film> films = new ArrayList<>();

        for (FilmCsv filmCsv : filmCsvList) {
            Film film = new Film();
            film.setTitle(filmCsv.getTitle());
            film.setDescription(filmCsv.getDescription());
            film.setReleaseYear(ParsingUtils.parseIntSafe(filmCsv.getReleaseYear()));

            film.setLanguage(
                    ParsingUtils.safeFindById(
                            ParsingUtils.parseIntSafe(filmCsv.getLanguage()) != null ? ParsingUtils.parseIntSafe(filmCsv.getLanguage()) : 8,
                            languageRepository,
                            "Language not found: " + filmCsv.getLanguage()
                    )
            );

            film.setOriginalLanguage(
                    ParsingUtils.safeFindById(
                            ParsingUtils.parseIntSafe(filmCsv.getOriginalLanguage()) != null ? ParsingUtils.parseIntSafe(filmCsv.getOriginalLanguage()) : 8,
                            languageRepository,
                            "Language not found: " + filmCsv.getOriginalLanguage()
                    )
            );

            film.setRentalDuration(ParsingUtils.parseIntSafe(filmCsv.getRentalDuration()));
            film.setRentalRate(ParsingUtils.parseDoubleSafe(filmCsv.getRentalRate()));
            film.setLength(ParsingUtils.parseIntSafe(filmCsv.getLength()));
            film.setReplacementCost(ParsingUtils.parseDoubleSafe(filmCsv.getReplacementCost()));
            film.setRating(ParsingUtils.parseEnumSafe(Rating.class, filmCsv.getRating().strip()));

            films.add(film);
        }

        filmRepository.saveAll(films);
    }
}
