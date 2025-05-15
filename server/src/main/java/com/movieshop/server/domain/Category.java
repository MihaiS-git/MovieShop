package com.movieshop.server.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"films"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String name;

    @Column(name="last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @PrePersist
    @PreUpdate
    public void updateTimestamp(){
        this.lastUpdate = LocalDateTime.now();
    }

    @ManyToMany(mappedBy = "categories")
    @Builder.Default
    private Set<Film> films = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }
}
