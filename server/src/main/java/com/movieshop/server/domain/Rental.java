package com.movieshop.server.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rental_date", nullable = false)
    private OffsetDateTime rentalDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(name = "return_date")
    private OffsetDateTime returnDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "staff_id", nullable = false)
    private User staff;

    @Column(name = "last_update", nullable = false)
    private OffsetDateTime lastUpdate;

    @Column(name = "rental_period", nullable = false)
    private Integer rentalPeriod;


    @PrePersist
    public void onCreate(){
        OffsetDateTime now = OffsetDateTime.now();
        if (rentalDate == null) {
            rentalDate = now;
        }
        if (rentalPeriod != null) {
            returnDate = rentalDate.plusDays(rentalPeriod);
        }
        lastUpdate = now;
    }

    @PreUpdate
    public void onUpdate(){
        lastUpdate = OffsetDateTime.now();
    }
}
