package com.movieshop.server.mapper;

import com.movieshop.server.domain.Store;
import com.movieshop.server.model.CustomerStoreResponseDTO;
import com.movieshop.server.model.StoreResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StoreMapper {

    public final AddressMapper addressMapper;

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


}
