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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private AutoCloseable closeable;

    private Film film;
    private Store store;
    private Inventory inventory;
    private InventoryRequestDTO requestDTO;
    private InventoryResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        film = new Film();
        film.setId(1);

        store = new Store();
        store.setId(1);

        inventory = new Inventory();
        inventory.setId(1);
        inventory.setFilm(film);
        inventory.setStore(store);

        requestDTO = InventoryRequestDTO.builder()
                .filmId(1)
                .storeId(1)
                .build();

        responseDTO = InventoryResponseDTO.builder()
                .id(1)
                .build();
    }

    @Test
    void createInventory_success() {
        when(filmRepository.findById(1)).thenReturn(Optional.of(film));
        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
        when(inventoryMapper.toResponseDto(inventory)).thenReturn(responseDTO);

        InventoryResponseDTO result = inventoryService.createInventory(requestDTO);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void createInventory_filmNotFound() {
        when(filmRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> inventoryService.createInventory(requestDTO));
        assertEquals("Film not found by id: 1", ex.getMessage());
    }

    @Test
    void createInventory_storeNotFound() {
        when(filmRepository.findById(1)).thenReturn(Optional.of(film));
        when(storeRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> inventoryService.createInventory(requestDTO));
        assertEquals("Store not found by id: 1", ex.getMessage());
    }

    @Test
    void getInventoryById_success() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(inventoryMapper.toResponseDto(inventory)).thenReturn(responseDTO);

        InventoryResponseDTO result = inventoryService.getInventoryById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void getInventoryById_notFound() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> inventoryService.getInventoryById(1));
        assertEquals("Inventory not found by id: 1", ex.getMessage());
    }

    @Test
    void getAllInventories_success() {
        when(inventoryRepository.findAll()).thenReturn(List.of(inventory));
        when(inventoryMapper.toResponseDto(inventory)).thenReturn(responseDTO);

        List<InventoryResponseDTO> result = inventoryService.getAllInventories();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void updateInventory_success() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(filmRepository.findById(1)).thenReturn(Optional.of(film));
        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
        when(inventoryMapper.toResponseDto(inventory)).thenReturn(responseDTO);

        InventoryResponseDTO result = inventoryService.updateInventory(1, requestDTO);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void updateInventory_inventoryNotFound() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> inventoryService.updateInventory(1, requestDTO));
        assertEquals("Inventory not found by id: 1", ex.getMessage());
    }

    @Test
    void deleteInventory_success() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));

        assertDoesNotThrow(() -> inventoryService.deleteInventory(1));
        verify(inventoryRepository).deleteById(1);
    }

    @Test
    void deleteInventory_notFound() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> inventoryService.deleteInventory(1));
        assertEquals("Inventory not found by id: 1", ex.getMessage());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
