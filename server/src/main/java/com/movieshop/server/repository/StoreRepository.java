package com.movieshop.server.repository;

import com.movieshop.server.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Integer> {
}
