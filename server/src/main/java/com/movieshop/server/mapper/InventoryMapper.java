package com.movieshop.server.mapper;

import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Inventory;
import com.movieshop.server.domain.Rental;
import com.movieshop.server.model.InventoryItemDTO;
import com.movieshop.server.model.InventoryResponseDTO;
import com.movieshop.server.model.MovieInventoryItemDTO;
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

    public InventoryItemDTO toInventoryItemDTO(Inventory inventory) {
        Film film = inventory.getFilm();

        return InventoryItemDTO.builder()
                .id(inventory.getId())
                .storeId(inventory.getStore().getId())
                .film(MovieInventoryItemDTO.builder()
                        .id(film.getId())
                        .title(film.getTitle())
                        .releaseYear(film.getReleaseYear())
                        .rentalRate(film.getRentalRate())
                        .replacementCost(film.getReplacementCost())
                        .build())
                .lastUpdate(inventory.getLastUpdate())
                .build();
    }
}
