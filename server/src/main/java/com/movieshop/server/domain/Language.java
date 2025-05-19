package com.movieshop.server.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = {"films", "originalLanguageFilms", "lastUpdate"})
@Builder
@Table(name="languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "last_update", nullable = false)
    private OffsetDateTime lastUpdate;

    public Language(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdate = OffsetDateTime.now();
    }

    @OneToMany(mappedBy = "language")
    private Set<Film> films;

    @OneToMany(mappedBy = "originalLanguage")
    private Set<Film> originalLanguageFilms;

    public Language(String name) {
        this.name = name;
    }
}
