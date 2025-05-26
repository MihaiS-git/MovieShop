package com.movieshop.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"inventory", "customer", "staff", "payments"})
@ToString(exclude = {"inventory", "customer", "staff", "payments"})
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rental_date", nullable = false)
    private OffsetDateTime rentalDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(name = "return_date", nullable = true)
    private OffsetDateTime returnDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_id", nullable = false)
    private User staff;

    @Column(name = "last_update", nullable = false)
    private OffsetDateTime lastUpdate;

    @Column(name = "rental_period", nullable = true)
    private Integer rentalPeriod;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rental")
    private List<Payment> payments = new ArrayList<>();


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

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setRental(this);
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
        payment.setRental(null);
    }
}
