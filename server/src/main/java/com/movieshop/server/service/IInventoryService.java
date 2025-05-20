package com.movieshop.server.service;

import com.movieshop.server.model.InventoryRequestDTO;
import com.movieshop.server.model.InventoryResponseDTO;

import java.util.List;

public interface IInventoryService {
    InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO);

    InventoryResponseDTO getInventoryById(Integer id);

    List<InventoryResponseDTO> getAllInventories();

    InventoryResponseDTO updateInventory(Integer id, InventoryRequestDTO inventoryRequestDTO);

    void deleteInventory(Integer id);
}
