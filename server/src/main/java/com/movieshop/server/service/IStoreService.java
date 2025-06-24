package com.movieshop.server.service;

import com.movieshop.server.model.StoreCompleteResponseDTO;
import com.movieshop.server.model.StorePageResponse;
import com.movieshop.server.model.StoreRequestDTO;
import com.movieshop.server.model.StoreResponseDTO;

public interface IStoreService {

    StoreCompleteResponseDTO getStoreById(Integer storeId);

    StoreResponseDTO createStore(StoreRequestDTO storeRequestDTO);

    StoreResponseDTO updateStore(Integer storeId, StoreRequestDTO storeRequestDTO);

    void deleteStore(Integer storeId);

    StorePageResponse getAllStoresPaginated(int page, int limit, String orderBy, String countryFilter, String cityFilter);

}
