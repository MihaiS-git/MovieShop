package com.movieshop.server.service;

import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Inventory;
import com.movieshop.server.domain.Store;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.InventoryMapper;
import com.movieshop.server.model.InventoryRequestDTO;
import com.movieshop.server.model.InventoryResponseDTO;
import com.movieshop.server.repository.FilmRepository;
import com.movieshop.server.repository.InventoryRepository;
import com.movieshop.server.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
