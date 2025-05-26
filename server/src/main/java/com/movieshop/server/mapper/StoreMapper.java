package com.movieshop.server.mapper;

import com.movieshop.server.domain.Store;
import com.movieshop.server.model.StoreResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {
    public StoreResponseDTO toResponseDto(Store store) {
        if (store == null) {
            return null;
        }
        return StoreResponseDTO.builder()
                .id(store.getId())
                .managerStaffId(store.getManagerStaff().getId())
                .addressId(store.getAddress().getId())
                .lastUpdate(store.getLastUpdate())
                .build();
    }
}
