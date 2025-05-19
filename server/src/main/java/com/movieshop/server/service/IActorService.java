package com.movieshop.server.service;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.model.ActorDTO;

import java.util.List;

public interface IActorService {

    List<ActorDTO> getAllActors();

    ActorDTO getActorById(Integer id);

    ActorDTO createActor(ActorDTO actorDTO);

    ActorDTO updateActor(Integer id, ActorDTO actorDTO);

    void deleteActor(Integer id);

}
