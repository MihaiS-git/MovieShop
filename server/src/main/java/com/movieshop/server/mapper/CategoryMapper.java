package com.movieshop.server.mapper;

import com.movieshop.server.domain.Category;
import com.movieshop.server.domain.Film;
import com.movieshop.server.model.CategoryRequestDTO;
import com.movieshop.server.model.CategoryResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDTO toResponseDto(Category category){
        if (category == null) return null;
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .lastUpdate(category.getLastUpdate())
                .filmIds(category.getFilms().stream()
                        .map(Film::getId)
                        .toList())
                .build();
    }

    public Category toEntity(CategoryRequestDTO categoryRequestDTO){
        if (categoryRequestDTO == null) return null;
        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        return category;
    }


}
