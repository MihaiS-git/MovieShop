package com.movieshop.server.mapper;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.model.ActorDTO;
import org.springframework.stereotype.Component;

@Component
public class ActorMapper {

    public ActorDTO toDto(Actor actor) {
        if (actor == null) return null;
        return ActorDTO.builder()
                .id(actor.getId())
                .firstName(actor.getFirstName())
                .lastName(actor.getLastName())
                .build();
    }

    public Actor toEntity(ActorDTO dto) {
        if (dto == null) return null;
        Actor actor = new Actor();
        actor.setId(dto.getId());
        actor.setFirstName(dto.getFirstName());
        actor.setLastName(dto.getLastName());
        return Actor.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
    }
}