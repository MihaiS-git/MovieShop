package com.movieshop.server.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"films"})
@EqualsAndHashCode(exclude = {"films", "lastUpdate"})
@Table(name="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name="last_update", nullable = false)
    private OffsetDateTime lastUpdate;

    @PrePersist
    @PreUpdate
    public void updateTimestamp(){
        this.lastUpdate = OffsetDateTime.now();
    }

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Film> films = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
