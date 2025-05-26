package com.movieshop.loader.loader;

import com.movieshop.loader.domain.Category;
import com.movieshop.loader.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryLoader {

    private final CategoryRepository categoryRepository;

    public void load() {
        if (categoryRepository.count() > 0) {
            return;
        }

        List<Category> categories = List.of(
                new Category("Action"),
                new Category("Animation"),
                new Category("Children"),
                new Category("Classics"),
                new Category("Comedy"),
                new Category("Documentary"),
                new Category("Drama"),
                new Category("Family"),
                new Category("Foreign"),
                new Category("Games"),
                new Category("Horror"),
                new Category("Music"),
                new Category("New"),
                new Category("Sci-Fi"),
                new Category("Sports"),
                new Category("Travel")
        );

        categoryRepository.saveAll(categories);
    }
}
