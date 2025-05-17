package com.movieshop.server.service;

import com.movieshop.server.domain.Category;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.model.CategoryDTO;
import com.movieshop.server.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Integer id) {
        return getCategoryByIdOrElseThrow(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Category not found with name: " + name));
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = new Category();
        newCategory.setId(null);
        newCategory.setName(categoryDTO.getName());
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category updateCategory(Integer id, CategoryDTO categoryDTO) {
        Category existentCategory = getCategoryByIdOrElseThrow(id);
        existentCategory.setName(categoryDTO.getName());
        return categoryRepository.save(existentCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = getCategoryByIdOrElseThrow(id);
        categoryRepository.deleteById(id);
    }

    private Category getCategoryByIdOrElseThrow(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category not found with id: " + id));
    }
}
