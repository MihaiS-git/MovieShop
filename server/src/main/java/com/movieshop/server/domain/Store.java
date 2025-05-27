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
@EqualsAndHashCode(exclude = {"inventories", "lastUpdate"})
@ToString(exclude = {"inventories"})
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_staff_id", unique = true, nullable = true)
    private User managerStaff;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "last_update", nullable = false)
    private OffsetDateTime lastUpdate;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        lastUpdate = OffsetDateTime.now();
    }

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Inventory> inventories = new HashSet<>();

    public void addInventory(Inventory inventory) {
        inventories.add(inventory);
        inventory.setStore(this);
    }

    public void removeInventory(Inventory inventory) {
        inventories.remove(inventory);
        inventory.setStore(null);
    }

    public void setManagerStaff(User manager) {
        this.managerStaff = manager;
        if (manager != null) {
            manager.setStore(this);
        }
    }
}
