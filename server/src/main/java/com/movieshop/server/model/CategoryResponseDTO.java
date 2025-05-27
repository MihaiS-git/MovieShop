package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDTO {

    private Integer id;

    private String name;

    private OffsetDateTime lastUpdate;

    private List<Integer> filmIds;

}
