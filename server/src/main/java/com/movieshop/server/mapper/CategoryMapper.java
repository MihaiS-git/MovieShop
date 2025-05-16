package com.movieshop.server.mapper;

import com.movieshop.server.domain.Category;
import com.movieshop.server.model.CategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDto(Category category){
        if (category == null) return null;
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toEntity(CategoryDTO categoryDTO){
        if (categoryDTO == null) return null;
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return category;
    }
}
