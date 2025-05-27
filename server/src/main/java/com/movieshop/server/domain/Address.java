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
@ToString(exclude = {"users", "stores"})
@EqualsAndHashCode(exclude = {"users", "stores", "lastUpdate"})
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String address;

    private String address2;

    private String district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    @NotNull
    private City city;

    @Column(name= "postal_code")
    private String postalCode;

    private String phone;

    @Column(name = "last_update")
    private OffsetDateTime lastUpdate;

    @PrePersist
    @PreUpdate
    private void updateTimeStamps() {
        lastUpdate = OffsetDateTime.now();
    }

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private Set<Store> stores = new HashSet<>();

}
