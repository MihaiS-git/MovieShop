package com.movieshop.loader.utils;

import com.movieshop.loader.domain.Rating;
import com.movieshop.loader.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

@Slf4j
public class ParsingUtils {
    public static Integer parseIntSafe(String s) {
        if (s == null || s.trim().isEmpty() || s.equalsIgnoreCase("null")) {
            return null;
        }
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static Double parseDoubleSafe(String s) {
        if (s == null || s.trim().isEmpty() || s.equalsIgnoreCase("null")) {
            return null;
        }
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static <T extends Enum<T>> T parseEnumSafe(Class<T> enumClass, String s) {
        if (s == null || s.trim().isEmpty() || s.equalsIgnoreCase("null")) {
            return null;
        }
        try {
            String trimmed = s.trim();
            if (enumClass.equals(Rating.class)) {
                return enumClass.cast(Rating.fromString(trimmed));
            }
            return Enum.valueOf(enumClass, trimmed);
        } catch (IllegalArgumentException e) {
            log.error("Invalid enum value for {}: {}", enumClass.getSimpleName(), s);
            return null;
        }
    }

    public static <T, ID> T safeFindById(ID id, JpaRepository<T, ID> repo, String notFoundMessage) {
        if (id == null) return null;
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }
}
