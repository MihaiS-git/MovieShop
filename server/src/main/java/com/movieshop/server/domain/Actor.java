package com.movieshop.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = {"films", "lastUpdate"})
@Table(name = "actors")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false)
    @NotNull
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotNull
    private String lastName;

    @Column(name = "last_update", nullable = false)
    private OffsetDateTime lastUpdate;

    public Actor(int d, String firstName, String lastName) {
        this.id = d;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdate = OffsetDateTime.now();
    }

    @ManyToMany(mappedBy = "actors")
    private Set<Film> films = new HashSet<>();

    public void addFilm(Film film) {
        films.add(film);
        film.getActors().add(this);
    }

    public void removeFilm(Film film) {
        films.remove(film);
        film.getActors().remove(this);
    }
}
