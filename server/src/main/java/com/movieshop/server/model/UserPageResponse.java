package com.movieshop.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserPageResponse {
    private List<UserResponseItemDTO> users;
    private long totalCount;
}
