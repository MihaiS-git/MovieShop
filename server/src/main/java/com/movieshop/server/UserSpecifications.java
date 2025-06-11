package com.movieshop.server;

import com.movieshop.server.domain.Role;
import com.movieshop.server.domain.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

public class UserSpecifications {

    public static Specification<User> hasRole(String role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null || role.isEmpty() || role.equalsIgnoreCase("ALL")) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("role"), Role.valueOf(role));
        };
    }

    public static Specification<User> generalSearchContains(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isBlank()) {
                return cb.conjunction();
            }

            String cleaned = searchTerm.trim().replaceAll("^\"|\"$", "").toLowerCase();
            String[] terms = cleaned.split("\\s+");

            if (terms.length != 2) {
                List<Predicate> predicates = Arrays.stream(terms)
                        .map(term -> cb.or(
                                cb.like(cb.lower(root.get("firstName")), "%" + term + "%"),
                                cb.like(cb.lower(root.get("lastName")), "%" + term + "%"),
                                cb.like(cb.lower(root.get("email")), "%" + term + "%")
                        ))
                        .toList();
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            String term1 = terms[0];
            String term2 = terms[1];

            Predicate firstLast = cb.and(
                    cb.like(cb.lower(root.get("firstName")), "%" + term1 + "%"),
                    cb.like(cb.lower(root.get("lastName")), "%" + term2 + "%")
            );

            Predicate lastFirst = cb.and(
                    cb.like(cb.lower(root.get("firstName")), "%" + term2 + "%"),
                    cb.like(cb.lower(root.get("lastName")), "%" + term1 + "%")
            );

            return cb.or(firstLast, lastFirst);
        };
    }


    public static Specification<User> isEnabled(Boolean enabled) {
        return (root, query, criteriaBuilder) -> {
            if (enabled == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("enabled"), enabled);
        };
    }

    public static Specification<User> isAccountNonExpired(Boolean accountNonExpired) {
        return (root, query, criteriaBuilder) -> {
            if (accountNonExpired == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("accountNonExpired"), accountNonExpired);
        };
    }

    public static Specification<User> isAccountNonLocked(Boolean accountNonLocked) {
        return (root, query, criteriaBuilder) -> {
            if (accountNonLocked == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("accountNonLocked"), accountNonLocked);
        };
    }

    public static Specification<User> isCredentialsNonExpired(Boolean credentialsNonExpired) {
        return (root, query, criteriaBuilder) -> {
            if (credentialsNonExpired == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("credentialsNonExpired"), credentialsNonExpired);
        };
    }
}
