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
@EqualsAndHashCode
@ToString
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(optional = false)
    @JoinColumn(name = "manager_staff_id", unique = true, nullable = false)
    private User managerStaff;

    @OneToOne(optional = false)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "last_update", nullable = false)
    private OffsetDateTime lastUpdate;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        lastUpdate = OffsetDateTime.now();
    }

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Inventory> inventories = new HashSet<>();

    public void addInventory(Inventory inventory) {
        inventories.add(inventory);
        inventory.setStore(this);
    }

    public void removeInventory(Inventory inventory) {
        inventories.remove(inventory);
        inventory.setStore(null);
    }
}
