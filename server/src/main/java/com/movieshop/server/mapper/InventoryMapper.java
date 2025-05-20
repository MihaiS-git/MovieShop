package com.movieshop.server.mapper;

import com.movieshop.server.domain.Inventory;
import com.movieshop.server.domain.Rental;
import com.movieshop.server.model.InventoryResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryResponseDTO toResponseDto(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        return InventoryResponseDTO.builder()
                .id(inventory.getId())
                .filmId(inventory.getFilm().getId())
                .storeId(inventory.getStore().getId())
                .lastUpdate(inventory.getLastUpdate())
                .rentalIds(inventory.getRentals() != null
                        ? (inventory.getRentals().stream()
                        .map(Rental::getId)
                        .toList())
                        : null)
                .build();
    }
}
