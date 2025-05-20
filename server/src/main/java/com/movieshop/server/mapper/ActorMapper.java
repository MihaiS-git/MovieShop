package com.movieshop.server.mapper;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Film;
import com.movieshop.server.model.ActorResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ActorMapper {

    public ActorResponseDTO toResponseDto(Actor actor) {
        if (actor == null) return null;
        return ActorResponseDTO.builder()
                .id(actor.getId())
                .firstName(actor.getFirstName())
                .lastName(actor.getLastName())
                .filmIds(actor.getFilms() == null ? null :
                        actor.getFilms().stream().map(Film::getId).toList())
                .build();
    }

    public Actor toEntity(ActorResponseDTO dto, Set<Film> films) {
        if (dto == null) return null;
        Actor actor = new Actor();
        actor.setFirstName(dto.getFirstName());
        actor.setLastName(dto.getLastName());
        actor.setFilms(films);
        return actor;
    }
}