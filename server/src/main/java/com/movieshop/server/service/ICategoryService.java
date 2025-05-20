package com.movieshop.server.service;

import com.movieshop.server.model.CategoryRequestDTO;
import com.movieshop.server.model.CategoryResponseDTO;

import java.util.List;

public interface ICategoryService {
    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryById(Integer id);

    CategoryResponseDTO getCategoryByName(CategoryRequestDTO categoryRequestDTO);

    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);

    CategoryResponseDTO updateCategory(Integer id, CategoryRequestDTO categoryRequestDTO);

    void deleteCategory(Integer id);

}
