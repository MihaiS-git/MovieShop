package com.movieshop.server;

import com.movieshop.server.domain.Store;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class StoreSpecifications {

    public static Specification<Store> hasCountry(String country){
        return (root, query, criteriaBuilder) -> {
            if(country == null || country.isEmpty() || country.equalsIgnoreCase("ALL")){
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> addressJoin = root.join("address", JoinType.INNER);

            Join<Object, Object> cityJoin = addressJoin.join("city", JoinType.INNER);

            Join<Object, Object> countryJoin = cityJoin.join("country", JoinType.INNER);

            return criteriaBuilder.equal(criteriaBuilder.lower(countryJoin.get("name")), country.toLowerCase());
        };
    }

    public static Specification<Store> hasCity(String city){
        return (root, query, criteriaBuilder) -> {
            if(city == null || city.isEmpty() || city.equalsIgnoreCase("ALL")){
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> addressJoin = root.join("address", JoinType.INNER);

            Join<Object, Object> cityJoin = addressJoin.join("city", JoinType.INNER);

            return criteriaBuilder.equal(criteriaBuilder.lower(cityJoin.get("name")), city.toLowerCase());
        };
    }
}
