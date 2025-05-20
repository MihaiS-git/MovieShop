package com.movieshop.server.service;

import com.movieshop.server.domain.Category;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.CategoryMapper;
import com.movieshop.server.model.CategoryRequestDTO;
import com.movieshop.server.model.CategoryResponseDTO;
import com.movieshop.server.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() {
        List<Category> categories = List.of(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getAllCategories_WhenEmpty_ShouldReturnEmptyList() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        assertTrue(result.isEmpty());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        Integer id = 1;
        Category category = new Category();
        category.setId(id);
        category.setName("Comedy");

        CategoryResponseDTO responseDTO = CategoryResponseDTO.builder()
                .id(id)
                .name("Comedy")
                .build();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.getCategoryById(id);

        assertNotNull(result);
        assertEquals("Comedy", result.getName());
        verify(categoryRepository).findById(id);
        verify(categoryMapper).toResponseDto(category);
    }


    @Test
    void getCategoryById_WhenNotFound_ShouldThrowException() {
        Integer id = 1;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(id);
        });

        assertEquals("Category not found with id: " + id, exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void getCategoryByName_ShouldReturnCategory() {
        CategoryRequestDTO categoryRequestDTO = CategoryRequestDTO.builder()
                .name("Action")
                .build();
        Category category = new Category("Action");
        CategoryResponseDTO categoryResponseDTO = CategoryResponseDTO.builder()
                .name("Action")
                .build();

        when(categoryRepository.findByName("Action")).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryResponseDTO);

        CategoryResponseDTO result = categoryService.getCategoryByName(categoryRequestDTO);

        assertNotNull(result);
        assertEquals("Action", result.getName());
        verify(categoryRepository).findByName("Action");
    }

    @Test
    void getCategoryByName_WhenNotFound_ShouldThrowException() {
        CategoryRequestDTO categoryRequestDTO = CategoryRequestDTO.builder()
                .name("Action")
                .build();

        when(categoryRepository.findByName(categoryRequestDTO.getName())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryByName(categoryRequestDTO);
        });

        assertEquals("Category not found with name: " + categoryRequestDTO.getName(), exception.getMessage());
        verify(categoryRepository, times(1)).findByName(categoryRequestDTO.getName());
    }

    @Test
    void createCategory_SavesAndReturnsCategory() {
        // Given
        CategoryRequestDTO dto = CategoryRequestDTO.builder()
                .name("Adventure")
                .build();

        Category category = new Category();
        category.setName("Adventure");

        Category savedCategory = new Category();
        savedCategory.setId(1);
        savedCategory.setName("Adventure");

        CategoryResponseDTO expectedResponse = CategoryResponseDTO.builder()
                .id(1)
                .name("Adventure")
                .build();

        // Mocking
        when(categoryMapper.toEntity(dto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toResponseDto(savedCategory)).thenReturn(expectedResponse);

        // When
        CategoryResponseDTO result = categoryService.createCategory(dto);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Adventure", result.getName());

        verify(categoryMapper).toEntity(dto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toResponseDto(savedCategory);
    }

    @Test
    void updateCategory_UpdatesAndReturnsCategory() {
        // Setup
        Integer id = 3;
        Category existing = new Category();
        existing.setId(id);
        existing.setName("OldName");

        CategoryRequestDTO dto = CategoryRequestDTO.builder()
                .name("NewName")
                .build();

        CategoryResponseDTO responseDTO = CategoryResponseDTO.builder()
                .id(id)
                .name("NewName")
                .build();

        // Mocks
        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(existing);
        when(categoryMapper.toResponseDto(existing)).thenReturn(responseDTO);

        // Call
        CategoryResponseDTO result = categoryService.updateCategory(id, dto);

        // Verify
        assertNotNull(result);
        assertEquals("NewName", result.getName());
        verify(categoryRepository).findById(id);
        verify(categoryRepository).save(existing);
        verify(categoryMapper).toResponseDto(existing);
    }


    @Test
    void updateCategory_WhenNotFound_ThrowsException() {
        when(categoryRepository.findById(3)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.updateCategory(3, new CategoryRequestDTO());
        });

        assertEquals("Category not found with id: 3", exception.getMessage());
        verify(categoryRepository, times(1)).findById(3);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void deleteCategory_DeletesCategory() {
        Category category = new Category();
        category.setId(3);
        category.setName("Action");

        when(categoryRepository.findById(3)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(3);

        assertDoesNotThrow(() -> categoryService.deleteCategory(3));
        verify(categoryRepository, times(1)).findById(3);
        verify(categoryRepository, times(1)).deleteById(3);
    }

    @Test
    void deleteCategory_WhenNotFound_ThrowsException() {
        when(categoryRepository.findById(3)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.deleteCategory(3);
        });

        assertEquals("Category not found with id: 3", exception.getMessage());
        verify(categoryRepository, times(1)).findById(3);
        verify(categoryRepository, never()).deleteById(any());
    }
}
