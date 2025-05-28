package com.movieshop.server;

import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Rating;
import org.springframework.data.jpa.domain.Specification;

public class FilmSpecifications {

    public static Specification<Film> hasRating(String rating) {
        return (root, query, criteriaBuilder) -> {
            if (rating == null || rating.isEmpty() || rating.equalsIgnoreCase("ALL")) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("rating"), Rating.valueOf(rating));
        };
    }

    public static Specification<Film> titleContains(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + searchTerm.toLowerCase() + "%");
        };
    }

    public static Specification<Film> releaseYearEquals(Integer releaseYear) {
        return (root, query, criteriaBuilder) -> {
            if (releaseYear == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("releaseYear"), releaseYear);
        };
    }

    public static Specification<Film> inCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null || category.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join("categories").get("name"), category);
        };
    }
}
