package com.movieshop.server.repository;

import com.movieshop.server.domain.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    @Query("SELECT i FROM Inventory i WHERE i.store.id = :storeId")
    Page<Inventory> findAllByStoreId(@Param("storeId") Integer storeId, Pageable pageable);
}
