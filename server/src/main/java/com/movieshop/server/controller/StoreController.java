package com.movieshop.server.controller;

import com.movieshop.server.model.StoreCompleteResponseDTO;
import com.movieshop.server.model.StorePageResponse;
import com.movieshop.server.model.StoreRequestDTO;
import com.movieshop.server.model.StoreResponseDTO;
import com.movieshop.server.service.IStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v0/stores")
public class StoreController {

    private final IStoreService storeService;

    public StoreController(IStoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<StorePageResponse> getAllStoresPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "id_asc") String orderBy,
            @RequestParam(required = false) String countryFilter,
            @RequestParam(required = false) String cityFilter
    ){
        return ResponseEntity.ok(storeService.getAllStoresPaginated(page, limit, orderBy, countryFilter, cityFilter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreCompleteResponseDTO> getStoreById(@PathVariable Integer id) {
        StoreCompleteResponseDTO store = storeService.getStoreById(id);
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
