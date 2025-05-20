package com.movieshop.server.service;

import com.movieshop.server.model.StoreRequestDTO;
import com.movieshop.server.model.StoreResponseDTO;

import java.util.List;

public interface IStoreService {

    List<StoreResponseDTO> getAllStores();

    StoreResponseDTO getStoreById(Integer storeId);

    StoreResponseDTO createStore(StoreRequestDTO storeRequestDTO);

    StoreResponseDTO updateStore(Integer storeId, StoreRequestDTO storeRequestDTO);

    void deleteStore(Integer storeId);

}
