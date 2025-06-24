//package com.movieshop.server.service;
//
//import com.movieshop.server.domain.Address;
//import com.movieshop.server.domain.Store;
//import com.movieshop.server.domain.User;
//import com.movieshop.server.exception.ResourceNotFoundException;
//import com.movieshop.server.mapper.StoreMapper;
//import com.movieshop.server.model.StoreRequestDTO;
//import com.movieshop.server.model.StoreResponseDTO;
//import com.movieshop.server.repository.AddressRepository;
//import com.movieshop.server.repository.StoreRepository;
//import com.movieshop.server.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class StoreServiceImplTest {
//
//    private StoreRepository storeRepository;
//    private StoreMapper storeMapper;
//    private UserRepository userRepository;
//    private AddressRepository addressRepository;
//    private StoreServiceImpl storeService;
//
//    @BeforeEach
//    void setUp() {
//        storeRepository = mock(StoreRepository.class);
//        storeMapper = mock(StoreMapper.class);
//        userRepository = mock(UserRepository.class);
//        addressRepository = mock(AddressRepository.class);
//        storeService = new StoreServiceImpl(storeRepository, storeMapper, userRepository, addressRepository);
//    }
//
//    @Test
//    void getAllStores_returnsList() {
//        Store store1 = new Store();
//        Store store2 = new Store();
//        when(storeRepository.findAll()).thenReturn(List.of(store1, store2));
//        when(storeMapper.toResponseDto(any(Store.class))).thenReturn(new StoreResponseDTO());
//
//        List<StoreResponseDTO> result = storeService.getAllStores();
//
//        assertEquals(2, result.size());
//        verify(storeRepository).findAll();
//        verify(storeMapper, times(2)).toResponseDto(any(Store.class));
//    }
//
//    @Test
//    void getStoreById_found_returnsDto() {
//        Store store = new Store();
//        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
//        when(storeMapper.toResponseDto(store)).thenReturn(new StoreResponseDTO());
//
//        StoreResponseDTO result = storeService.getStoreById(1);
//
//        assertNotNull(result);
//        verify(storeRepository).findById(1);
//        verify(storeMapper).toResponseDto(store);
//    }
//
//    @Test
//    void getStoreById_notFound_throws() {
//        when(storeRepository.findById(1)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> storeService.getStoreById(1));
//        verify(storeRepository).findById(1);
//    }
//
//    @Test
//    void createStore_validInput_returnsDto() {
//        StoreRequestDTO request = StoreRequestDTO.builder()
//                .managerStaffId(10)
//                .addressId(20)
//                .build();
//
//        User manager = new User();
//        Address address = new Address();
//        Store savedStore = new Store();
//
//        when(userRepository.findById(10)).thenReturn(Optional.of(manager));
//        when(addressRepository.findById(20)).thenReturn(Optional.of(address));
//        when(storeRepository.save(any(Store.class))).thenReturn(savedStore);
//        when(storeMapper.toResponseDto(savedStore)).thenReturn(new StoreResponseDTO());
//
//        StoreResponseDTO result = storeService.createStore(request);
//
//        assertNotNull(result);
//        verify(userRepository).findById(10);
//        verify(addressRepository).findById(20);
//        verify(storeRepository).save(any(Store.class));
//        verify(storeMapper).toResponseDto(savedStore);
//    }
//
//    @Test
//    void createStore_invalidManager_throws() {
//        StoreRequestDTO request = StoreRequestDTO.builder()
//                .managerStaffId(10)
//                .addressId(20)
//                .build();
//
//        when(userRepository.findById(10)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> storeService.createStore(request));
//        verify(userRepository).findById(10);
//        verify(addressRepository, never()).findById(anyInt());
//    }
//
//    @Test
//    void createStore_invalidAddress_throws() {
//        StoreRequestDTO request = StoreRequestDTO.builder()
//                .managerStaffId(10)
//                .addressId(20)
//                .build();
//
//        when(userRepository.findById(10)).thenReturn(Optional.of(new User()));
//        when(addressRepository.findById(20)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> storeService.createStore(request));
//        verify(addressRepository).findById(20);
//    }
//
//    @Test
//    void updateStore_validInput_returnsDto() {
//        StoreRequestDTO request = StoreRequestDTO.builder()
//                .managerStaffId(10)
//                .addressId(20)
//                .build();
//
//        Store existingStore = new Store();
//        User newManager = new User();
//        Address newAddress = new Address();
//        Store updatedStore = new Store();
//
//        when(storeRepository.findById(1)).thenReturn(Optional.of(existingStore));
//        when(userRepository.findById(10)).thenReturn(Optional.of(newManager));
//        when(addressRepository.findById(20)).thenReturn(Optional.of(newAddress));
//        when(storeRepository.save(existingStore)).thenReturn(updatedStore);
//        when(storeMapper.toResponseDto(updatedStore)).thenReturn(new StoreResponseDTO());
//
//        StoreResponseDTO result = storeService.updateStore(1, request);
//
//        assertNotNull(result);
//        verify(storeRepository).findById(1);
//        verify(userRepository).findById(10);
//        verify(addressRepository).findById(20);
//        verify(storeRepository).save(existingStore);
//        verify(storeMapper).toResponseDto(updatedStore);
//    }
//
//    @Test
//    void updateStore_notFound_throws() {
//        StoreRequestDTO request = new StoreRequestDTO();
//        when(storeRepository.findById(1)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> storeService.updateStore(1, request));
//    }
//
//    @Test
//    void deleteStore_found_deletes() {
//        Store store = new Store();
//        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
//
//        storeService.deleteStore(1);
//
//        verify(storeRepository).findById(1);
//        verify(storeRepository).deleteById(1);
//    }
//
//    @Test
//    void deleteStore_notFound_throws() {
//        when(storeRepository.findById(1)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> storeService.deleteStore(1));
//        verify(storeRepository).findById(1);
//        verify(storeRepository, never()).deleteById(anyInt());
//    }
//}
