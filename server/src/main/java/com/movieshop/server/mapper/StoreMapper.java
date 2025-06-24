package com.movieshop.server.mapper;

import com.movieshop.server.domain.Store;
import com.movieshop.server.model.CustomerStoreResponseDTO;
import com.movieshop.server.model.StoreCompleteResponseDTO;
import com.movieshop.server.model.StoreResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class StoreMapper {

    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final InventoryMapper inventoryMapper;

    public StoreResponseDTO toResponseDto(Store store) {
        if (store == null) {
            return null;
        }
        return StoreResponseDTO.builder()
                .id(store.getId())
                .managerStaffId(store.getManagerStaff().getId())
                .addressId(store.getAddress() != null ? store.getAddress().getId() : null)
                .lastUpdate(store.getLastUpdate())
                .build();
    }

    public CustomerStoreResponseDTO toCustomerResponseDto(Store store) {
        if (store == null) {
            return null;
        }
        return CustomerStoreResponseDTO.builder()
                .id(store.getId())
                .managerStaffId(store.getManagerStaff().getId())
                .lastUpdate(store.getLastUpdate())
                .build();
    }

    public StoreCompleteResponseDTO toResponseWithDetailsDto(Store store) {
        if (store == null) {
            return null;
        }
        return StoreCompleteResponseDTO.builder()
                .id(store.getId())
                .managerStaff(userMapper.toResponseWithAddressDto(store.getManagerStaff()))
                .address(addressMapper.toResponseDto(store.getAddress()))
                .inventories(store.getInventories().stream().map(inventoryMapper::toResponseDto).collect(Collectors.toSet()))
                .lastUpdate(store.getLastUpdate())
                .build();

    }
}
