package com.movieshop.server.mapper;

import com.movieshop.server.domain.Film;
import com.movieshop.server.model.FilmDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FilmMapper {

    FilmDTO toDto(Film film);
    Film toEntity(FilmDTO filmDTO);
}
