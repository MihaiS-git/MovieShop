package com.movieshop.server.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    @EqualsAndHashCode.Include
    private Integer id;

    private String title;

    private String description;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "language_id")
    private Integer languageId;

    @Column(name = "original_language_id")
    private Integer originalLanguageId;

    @Column(name="rental_duration")
    private Integer rentalDuration;

    @Column(name = "rental_rate")
    private Double rentalRate;

    private Integer length;

    @Column(name="replacement_cost")
    private Double replacementCost;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    public Film(String title,
                String description,
                Integer releaseYear,
                Integer languageId,
                Integer originalLanguageId,
                Integer rentalDuration,
                Double rentalRate,
                Integer length,
                Double replacementCost,
                String rating,
                String lastUpdate
    ) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.languageId = languageId;
        this.originalLanguageId = originalLanguageId;
        this.rentalDuration = rentalDuration;
        this.rentalRate = rentalRate;
        this.length = length;
        this.replacementCost = replacementCost;
        this.rating = Rating.fromString(rating);
        this.lastUpdate = lastUpdate != null ? OffsetDateTime.parse(lastUpdate).toLocalDateTime() : LocalDateTime.now();
    }
}
