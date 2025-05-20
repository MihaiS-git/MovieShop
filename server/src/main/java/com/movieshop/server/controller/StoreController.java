package com.movieshop.server.controller;

import com.movieshop.server.domain.Store;
import com.movieshop.server.model.StoreRequestDTO;
import com.movieshop.server.model.StoreResponseDTO;
import com.movieshop.server.service.IStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/stores")
public class StoreController {

    private final IStoreService storeService;

    public StoreController(IStoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<List<StoreResponseDTO>> getAllStores() {
        List<StoreResponseDTO> stores = storeService.getAllStores();
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> getStoreById(Integer id) {
        StoreResponseDTO store = storeService.getStoreById(id);
        return ResponseEntity.ok(store);
    }

    @PostMapping
    public ResponseEntity<StoreResponseDTO> createStore(@RequestBody StoreRequestDTO storeRequestDTO) {
        StoreResponseDTO createdStore = storeService.createStore(storeRequestDTO);
        return ResponseEntity.status(201).body(createdStore);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> updateStore(@PathVariable Integer id, @RequestBody StoreRequestDTO storeRequestDTO) {
        StoreResponseDTO updatedStore = storeService.updateStore(id, storeRequestDTO);
        return ResponseEntity.ok(updatedStore);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Integer id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}
