package com.movieshop.server.repository;

import com.movieshop.server.domain.Store;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Integer>, JpaSpecificationExecutor<Store> {

    @Override
    @EntityGraph(attributePaths = {"managerStaff", "address", "address.city", "address.city.country"})
    @NonNull
    Page<Store> findAll(Specification<Store> spec, @NonNull Pageable pageable);

    @Query("""
            SELECT s from Store s
            LEFT JOIN FETCH s.managerStaff m
            LEFT JOIN FETCH m.address ma
            LEFT JOIN FETCH ma.city mac
            LEFT JOIN FETCH mac.country
            LEFT JOIN FETCH s.address a
            LEFT JOIN FETCH a.city ac
            LEFT JOIN FETCH ac.country
            LEFT JOIN FETCH s.inventories i
            WHERE s.id = :id
            """)
    Optional<Store> findByIdWithDetails(@Param("id") Integer id);
}
