package com.movieshop.server.service;

import com.movieshop.server.domain.Category;
import com.movieshop.server.model.CategoryDTO;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(Integer id);

    Category getCategoryByName(String name);

    Category createCategory(CategoryDTO categoryDTO);

    Category updateCategory(Integer id, CategoryDTO categoryDTO);

    void deleteCategory(Integer id);

}
