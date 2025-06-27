package com.movieshop.server.repository;

import com.movieshop.server.domain.User;
import com.movieshop.server.model.UserWithAddressProjection;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("""
                SELECT u FROM User u
                LEFT JOIN FETCH u.address a
                LEFT JOIN FETCH a.city c
                LEFT JOIN FETCH c.country
                WHERE u.email = :email
            """)
    Optional<User> findByEmailWithAddress(@Email @NotBlank String email);

    @Query("""
                SELECT u FROM User u
                LEFT JOIN FETCH u.address a
                LEFT JOIN FETCH a.city c
                LEFT JOIN FETCH c.country co
                WHERE u.id = :userId
            """)
    Optional<UserWithAddressProjection> findUserWithAddressById(@Param("userId") Integer userId);

    @Query("select r.id from Rental r where r.customer.id = :userId")
    List<Integer> findRentalIdsByUserId(@Param("userId") Integer userId);

    @Query("select p.id from Payment p where p.customer.id = :userId")
    List<Integer> findCustomerPaymentIdsByUserId(@Param("userId") Integer userId);

    @Query("select p.id from Payment p where p.staff.id = :userId")
    List<Integer> findStaffPaymentIdsByUserId(@Param("userId") Integer userId);

    Page<User> findAll(Specification<User> spec, Pageable pageable);
}
