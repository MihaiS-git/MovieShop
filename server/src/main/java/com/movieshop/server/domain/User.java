package com.movieshop.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name= "first_name", nullable = false)
    private String firstName;

    @Column(name= "last_name", nullable = false)
    private String lastName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id")
    private Store store;

    private String picture;

    @OneToMany(mappedBy = "staff")
    private List<Rental> rentals = new ArrayList<>();

    @Column(name="account_non_expired")
    private boolean accountNonExpired = true;

    @Column(name="account_non_locked")
    private boolean accountNonLocked = true;

    @Column(name="credentials_non_expired")
    private boolean credentialsNonExpired = true;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name="create_date", nullable = false)
    private OffsetDateTime createAt;

    @Column(name = "last_update", nullable = false)
    private OffsetDateTime lastUpdate;

    @PrePersist
    public void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createAt = now;
        this.lastUpdate = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.lastUpdate = OffsetDateTime.now();
    }


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void addRental(Rental rental) {
        if (rental != null) {
            rentals.add(rental);
            rental.setStaff(this);
        }
    }

    public void removeRental(Rental rental) {
        if (rental != null) {
            rentals.remove(rental);
            rental.setStaff(null);
        }
    }
}
