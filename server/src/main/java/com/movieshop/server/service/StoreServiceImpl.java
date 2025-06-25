package com.movieshop.server.service;

import com.movieshop.server.StoreSpecifications;
import com.movieshop.server.domain.*;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.AddressMapper;
import com.movieshop.server.mapper.StoreMapper;
import com.movieshop.server.model.*;
import com.movieshop.server.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StoreServiceImpl implements IStoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public StoreServiceImpl(
            StoreRepository storeRepository,
            StoreMapper storeMapper,
            UserRepository userRepository,
            AddressRepository addressRepository,
            AddressMapper addressMapper
    ) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Transactional
    @Override
    public StoreCompleteResponseDTO getStoreById(Integer storeId) {
        Store store = storeRepository.findByIdWithDetails(storeId).orElseThrow(() ->
                new ResourceNotFoundException("Store not found with id: " + storeId));
        return storeMapper.toResponseWithDetailsDto(store);
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

    @Override
    public StorePageResponse getAllStoresPaginated(int page, int limit, String orderBy, String countryFilter, String cityFilter) {
        Sort sort = parseOrderBy(orderBy);

        Pageable pageable = PageRequest.of(page, limit, sort);

        Specification<Store> spec = Specification
                .where(StoreSpecifications.hasCountry(countryFilter))
                .and(StoreSpecifications.hasCity(cityFilter));

        Page<Store> storesPage = storeRepository.findAll(spec, pageable);

        List<StoreDetailsResponseDTO> storeDTOs = storesPage.getContent().stream()
                .map(store -> {
                    User manager = store.getManagerStaff();
                    UserManagerResponseDTO managerDto = null;
                    if (manager != null) {
                        managerDto = UserManagerResponseDTO.builder()
                                .id(manager.getId())
                                .firstName(manager.getFirstName())
                                .lastName(manager.getLastName())
                                .build();
                    }

                    AddressResponseDTO addressDto = null;
                    if (store.getAddress() != null) {
                        addressDto = addressMapper.toResponseDto(store.getAddress());
                    }

                    return StoreDetailsResponseDTO.builder()
                            .id(store.getId())
                            .managerStaff(managerDto)
                            .address(addressDto)
                            .lastUpdate(store.getLastUpdate())
                            .build();
                }).toList();

        return new StorePageResponse(storeDTOs, storesPage.getTotalElements());
    }

    private Sort parseOrderBy(String orderBy) {
        if (orderBy == null || orderBy.isBlank() || orderBy.equalsIgnoreCase("None")) {
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
