package com.movieshop.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StorePageResponse {
    private List<StoreDetailsResponseDTO> stores;
    private long totalCount;
}
