package com.movieshop.server.service;

import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Inventory;
import com.movieshop.server.domain.Store;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.InventoryMapper;
import com.movieshop.server.model.*;
import com.movieshop.server.repository.FilmRepository;
import com.movieshop.server.repository.InventoryRepository;
import com.movieshop.server.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class InventoryServiceImpl implements IInventoryService {

    private final InventoryRepository inventoryRepository;
    private final FilmRepository filmRepository;
    private final StoreRepository storeRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(
            InventoryRepository inventoryRepository,
            FilmRepository filmRepository,
            StoreRepository storeRepository,
            InventoryMapper inventoryMapper
    ) {
        this.inventoryRepository = inventoryRepository;
        this.filmRepository = filmRepository;
        this.storeRepository = storeRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO) {
        Inventory inventory = new Inventory();
        Film film = filmRepository.findById(inventoryRequestDTO.getFilmId())
                .orElseThrow(() -> new ResourceNotFoundException("Film not found by id: " + inventoryRequestDTO.getFilmId()));
        Store store = storeRepository.findById(inventoryRequestDTO.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found by id: " + inventoryRequestDTO.getStoreId()));

        film.addInventory(inventory);
        store.addInventory(inventory);

        inventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponseDto(inventory);
    }

    @Override
    public InventoryResponseDTO getInventoryById(Integer id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found by id: " + id));
        return inventoryMapper.toResponseDto(inventory);
    }

    @Override
    public List<InventoryResponseDTO> getAllInventories() {
        List<Inventory> inventories = inventoryRepository.findAll();
        return inventories.stream().map(inventoryMapper::toResponseDto).toList();
    }

    @Override
    public InventoryResponseDTO updateInventory(Integer id, InventoryRequestDTO inventoryRequestDTO) {
        Inventory existentInventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found by id: " + id));
        Film film = filmRepository.findById(inventoryRequestDTO.getFilmId())
                .orElseThrow(() -> new ResourceNotFoundException("Film not found by id: " + inventoryRequestDTO.getFilmId()));
        Store store = storeRepository.findById(inventoryRequestDTO.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found by id: " + inventoryRequestDTO.getStoreId()));
        existentInventory.setFilm(film);
        existentInventory.setStore(store);
        existentInventory = inventoryRepository.save(existentInventory);
        return inventoryMapper.toResponseDto(existentInventory);
    }

    @Override
    public void deleteInventory(Integer id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found by id: " + id));
        inventoryRepository.deleteById(id);
    }

    @Transactional
    @Override
    public InventoryPageResponse getAllInventoriesByStoreIdPaginated(Integer storeId, Integer page, Integer limit, String orderBy) {
        Sort sort = parseOrderBy(orderBy);
        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<Inventory> inventoriesPage = inventoryRepository.findAllByStoreId(storeId, pageable);

        List<Inventory> inventories = inventoriesPage.getContent();

        List<InventoryItemDTO> inventoryDtos = inventoriesPage.getContent().stream()
                .map(inventoryMapper::toInventoryItemDTO)
                .toList();

        return new InventoryPageResponse(
                inventoryDtos,
                (int) inventoriesPage.getTotalElements(),
                inventoriesPage.getNumber(),
                inventoriesPage.getSize(),
                inventoriesPage.getTotalPages(),
                inventoriesPage.isLast()
        );
    }

    private Sort parseOrderBy(String orderBy) {
        if (orderBy == null || orderBy.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "id");
        }
        String[] parts = orderBy.split("_", 2);
        if (parts.length != 2) {
            log.warn("Invalid orderBy format '{}', falling back to id_asc", orderBy);
            return Sort.by(Sort.Direction.ASC, "id");
        }
        String field = parts[0];
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(parts[1]);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid sort direction '{}', defaulting to ASC", parts[1]);
            direction = Sort.Direction.ASC;
        }
        return Sort.by(direction, field);
    }
}
