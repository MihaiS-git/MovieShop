package com.movieshop.server.service;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.Store;
import com.movieshop.server.domain.User;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.StoreMapper;
import com.movieshop.server.model.StoreRequestDTO;
import com.movieshop.server.model.StoreResponseDTO;
import com.movieshop.server.repository.AddressRepository;
import com.movieshop.server.repository.StoreRepository;
import com.movieshop.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements IStoreService{

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public StoreServiceImpl(
            StoreRepository storeRepository,
            StoreMapper storeMapper,
            UserRepository userRepository,
            AddressRepository addressRepository
    ) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<StoreResponseDTO> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream().map(storeMapper::toResponseDto).toList();
    }

    @Override
    public StoreResponseDTO getStoreById(Integer storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new ResourceNotFoundException("Store not found with id: " + storeId));
        return storeMapper.toResponseDto(store);
    }

    @Override
    public StoreResponseDTO createStore(StoreRequestDTO storeRequestDTO) {
        User managerStaff = userRepository.findById(storeRequestDTO.getManagerStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager staff not found with id: " + storeRequestDTO.getManagerStaffId()));
        Address address = addressRepository.findById(storeRequestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + storeRequestDTO.getAddressId()));
        Store store = new Store();
        store.setManagerStaff(managerStaff);
        store.setAddress(address);

        store = storeRepository.save(store);
        return storeMapper.toResponseDto(store);
    }

    @Override
    public StoreResponseDTO updateStore(Integer storeId, StoreRequestDTO storeRequestDTO) {
        Store existentStore = storeRepository.findById(storeId).orElseThrow(() ->
                new ResourceNotFoundException("Store not found with id: " + storeId));
        User managerStaff = userRepository.findById(storeRequestDTO.getManagerStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager staff not found with id: " + storeRequestDTO.getManagerStaffId()));
        Address address = addressRepository.findById(storeRequestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + storeRequestDTO.getAddressId()));
        existentStore.setManagerStaff(managerStaff);
        existentStore.setAddress(address);

        existentStore = storeRepository.save(existentStore);
        return storeMapper.toResponseDto(existentStore);
    }

    @Override
    public void deleteStore(Integer storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new ResourceNotFoundException("Store not found with id: " + storeId));
        storeRepository.deleteById(storeId);
    }
}
