package com.movieshop.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InventoryPageResponse {
    List<InventoryItemDTO> inventories;
    Integer totalCount;
    Integer currentPage;
    Integer pageSize;
    Integer totalPages;
    Boolean isLastPage;
}
