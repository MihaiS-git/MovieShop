package com.movieshop.loader.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmCsv {

    @CsvBindByName(column = "title")
    private String title;

    @CsvBindByName(column = "description")
    private String description;

    @CsvBindByName(column = "release_year")
    private String releaseYear;

    @CsvBindByName(column = "language")
    private String language;

    @CsvBindByName(column = "original_language")
    private String originalLanguage;

    @CsvBindByName(column = "rental_duration")
    private String rentalDuration;

    @CsvBindByName(column = "rental_rate")
    private String rentalRate;

    @CsvBindByName(column = "length")
    private String length;

    @CsvBindByName(column = "replacement_cost")
    private String replacementCost;

    @CsvBindByName(column = "rating")
    private String rating;

}
