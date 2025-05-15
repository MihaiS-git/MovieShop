package com.movieshop.server.service;

import com.movieshop.server.domain.Category;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.model.CategoryDTO;
import com.movieshop.server.repository.CategoryRepository;
import com.movieshop.server.repository.FilmRepository;
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
    private FilmRepository filmRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp(){
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories(){
        List<Category> categories = List.of(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById_ShouldReturnCategory(){
        Integer id = 1;
        Category category = new Category();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(id);

        assertNotNull(result);
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void getCategoryById_WhenNotFound_ShouldThrowException(){
        Integer id = 1;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(id);
        });

        assertEquals("Category not found with id: " + id, exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void getCategoryByName_ShouldReturnCategory(){
        String name = "Action";
        Category category = new Category("Action");

        when(categoryRepository.findByName(name)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryByName(name);

        assertNotNull(result);
        assertEquals("Action", result.getName());
        verify(categoryRepository, times(1)).findByName(name);
    }

    @Test
    void getCategoryByName_WhenNotFound_ShouldThrowException(){
        String name = "Action";

        when(categoryRepository.findByName(name)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryByName(name);
        });

        assertEquals("Category not found with name: " + name, exception.getMessage());
        verify(categoryRepository, times(1)).findByName(name);
    }

    @Test
    void createCategory_SavesAndReturnsCategory(){
        CategoryDTO dto = new CategoryDTO(10, "Adventure");
        Category category = Category.builder()
                .name(dto.getName())
                .build();
        Category expected = Category.builder()
                .id(10)
                .name("Adventure")
                .build();

        when(categoryRepository.save(category)).thenReturn(expected);

        Category result = categoryService.createCategory(dto);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getName(), result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void updateCategory_UpdatesAndReturnsCategory(){
        Category existing = Category.builder()
                .id(3)
                .name("OldName")
                .build();
        Integer id = 3;
        CategoryDTO dto = new CategoryDTO("NewName");

        when(categoryRepository.findById(3)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(existing);

        Category result = categoryService.updateCategory(id, dto);

        assertNotNull(result);
        assertEquals("NewName", result.getName());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).save(existing);
    }

    @Test
    void updateCategory_WhenNotFound_ThrowsException() {
        when(categoryRepository.findById(3)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.updateCategory(3, new CategoryDTO());
        });

        assertEquals("Category not found with id: 3", exception.getMessage());
        verify(categoryRepository, times(1)).findById(3);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void deleteCategory_DeletesCategory() {
        Category category = Category.builder()
                .id(3)
                .name("Action")
                .build();

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
