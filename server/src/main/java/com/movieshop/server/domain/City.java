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
@ToString(exclude = {"addresses"})
@EqualsAndHashCode(exclude = {"addresses", "lastUpdate"})
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @OneToMany(mappedBy="city", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();

    @Column(name = "last_update")
    private OffsetDateTime lastUpdate;

    public City(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    @PrePersist
    @PreUpdate
    private void updateTimeStamps(){
        lastUpdate = OffsetDateTime.now();
    }

    public void addAddress(Address address) {
        addresses.add(address);
        address.setCity(this);
    }

    public void removeAddress(Address address){
        addresses.remove(address);
        address.setCity(null);
    }
}
