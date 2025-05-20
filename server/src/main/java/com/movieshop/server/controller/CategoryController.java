package com.movieshop.server.controller;

import com.movieshop.server.model.CategoryRequestDTO;
import com.movieshop.server.model.CategoryResponseDTO;
import com.movieshop.server.service.ICategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<CategoryResponseDTO> getCategoryByName(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok(categoryService.getCategoryByName(categoryRequestDTO));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Integer id, @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
