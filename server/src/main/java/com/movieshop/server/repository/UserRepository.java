package com.movieshop.server.repository;

import com.movieshop.server.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("""
                SELECT u FROM User u
                LEFT JOIN FETCH u.address a
                LEFT JOIN FETCH a.city c
                LEFT JOIN FETCH c.country
                LEFT JOIN FETCH u.store
                WHERE u.email = :email
            """)
    Optional<User> findByEmailWithAddressAndStore(@Email @NotBlank String email);

    Page<User> findAll(Specification<User> spec, Pageable pageable);
}
