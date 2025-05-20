package com.movieshop.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"categories", "actors", "inventories"})
@EqualsAndHashCode(exclude = {"actors", "categories", "inventories", "lastUpdate"})
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Integer id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @Column(name = "release_year")
    @Min(1930)
    @Max(2050)
    @NotNull
    private Integer releaseYear;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    @Column(name = "rental_duration")
    @Min(1)
    private Integer rentalDuration;

    @Column(name = "rental_rate")
    @Min(0)
    private Double rentalRate;

    @Min(1)
    private Integer length;

    @Column(name = "replacement_cost")
    @Min(0)
    private Double replacementCost;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "last_update", nullable = false)
    @PastOrPresent
    private OffsetDateTime lastUpdate;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdate = OffsetDateTime.now();
    }

    @ManyToMany
    @JoinTable(
            name = "film_category",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors = new HashSet<>();

    public void addCategory(Category category) {
        categories.add(category);
        category.getFilms().add(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getFilms().remove(this);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
        actor.getFilms().add(this);
    }

    public void removeActor(Actor actor) {
        actors.remove(actor);
        actor.getFilms().remove(this);
    }

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Inventory> inventories = new HashSet<>();

    public void addInventory(Inventory inventory) {
        inventories.add(inventory);
        inventory.setFilm(this);
    }

    public void removeInventory(Inventory inventory) {
        inventories.remove(inventory);
        inventory.setFilm(null);
    }


    public Film(@NotNull String title,
                @NotNull String description,
                @NotNull Integer releaseYear,
                Language language,
                Language originalLanguage,
                Integer rentalDuration,
                Double rentalRate,
                Integer length,
                Double replacementCost,
                String rating
    ) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.language = language;
        this.originalLanguage = originalLanguage;
        this.rentalDuration = rentalDuration;
        this.rentalRate = rentalRate;
        this.length = length;
        this.replacementCost = replacementCost;
        this.rating = Rating.fromString(rating);
    }
}
