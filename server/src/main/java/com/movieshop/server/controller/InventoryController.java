package com.movieshop.server.controller;

import com.movieshop.server.model.InventoryPageResponse;
import com.movieshop.server.model.InventoryRequestDTO;
import com.movieshop.server.model.InventoryResponseDTO;
import com.movieshop.server.service.IInventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v0/inventory")
public class InventoryController {

    private final IInventoryService inventoryService;

    public InventoryController(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventories() {
        List<InventoryResponseDTO> inventoryResponseDTOs = inventoryService.getAllInventories();
        return ResponseEntity.ok(inventoryResponseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponseDTO> getInventoryById(@PathVariable Integer id) {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventoryResponseDTO);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<InventoryPageResponse> getInventoriesByStoreId(
            @PathVariable Integer storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "id_asc") String orderBy
    ){
        InventoryPageResponse inventories = inventoryService.getAllInventoriesByStoreIdPaginated(storeId, page, limit, orderBy);
        return ResponseEntity.ok(inventories);
    }

    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createInventory(@Valid @RequestBody InventoryRequestDTO inventoryRequestDTO){
        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory(inventoryRequestDTO);
        URI location = URI.create("/api/v0/inventory/" + inventoryResponseDTO.getId());
        return ResponseEntity.created(location).body(inventoryResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponseDTO> updateInventory(@PathVariable Integer id, @Valid @RequestBody InventoryRequestDTO inventoryRequestDTO) {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.updateInventory(id, inventoryRequestDTO);
        if (inventoryResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inventoryResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}
