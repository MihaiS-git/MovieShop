package com.movieshop.server.service;

import com.movieshop.server.domain.Category;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.CategoryMapper;
import com.movieshop.server.model.CategoryRequestDTO;
import com.movieshop.server.model.CategoryResponseDTO;
import com.movieshop.server.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }

    @Override
    public CategoryResponseDTO getCategoryById(Integer id) {
        Category category = getCategoryByIdOrElseThrow(id);
        return categoryMapper.toResponseDto(category);
    }

    @Override
    public CategoryResponseDTO getCategoryByName(CategoryRequestDTO categoryRequestDTO) {
        String name = categoryRequestDTO.getName();
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category newCategory = categoryMapper.toEntity(categoryRequestDTO);
        newCategory = categoryRepository.save(newCategory);
        return categoryMapper.toResponseDto(newCategory);
    }

    @Override
    public CategoryResponseDTO updateCategory(Integer id, CategoryRequestDTO categoryRequestDTO) {
        Category existentCategory = getCategoryByIdOrElseThrow(id);
        existentCategory.setName(categoryRequestDTO.getName());

        existentCategory = categoryRepository.save(existentCategory);
        return categoryMapper.toResponseDto(existentCategory);
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
