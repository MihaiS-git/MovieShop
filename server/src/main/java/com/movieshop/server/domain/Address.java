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
@ToString(exclude = {"staff", "stores", "customers"})
@EqualsAndHashCode(exclude = {"staff", "stores", "customers", "lastUpdate"})
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String address;

    private String address2;

    private String district;

    @ManyToOne
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

    @OneToMany(mappedBy = "address")
    private Set<User> staff = new HashSet<>();

    @OneToMany(mappedBy = "address")
    private Set<Store> stores = new HashSet<>();

    @OneToMany(mappedBy = "address")
    private Set<User> customers = new HashSet<>();

    public void addStaff(User staff) {
        this.staff.add(staff);
        staff.setAddress(this);
    }

    public void removeStaff(User staff) {
        this.staff.remove(staff);
        staff.setAddress(null);
    }

    public void addStore(Store store) {
        this.stores.add(store);
        store.setAddress(this);
    }

    public void removeStore(Store store) {
        this.stores.remove(store);
        store.setAddress(null);
    }

    public void addCustomer(User customer) {
        this.customers.add(customer);
        customer.setAddress(this);
    }

    public void removeCustomer(User customer) {
        this.customers.remove(customer);
        customer.setAddress(null);
    }
}
