package com.movieshop.server.service;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.model.ActorDTO;

import java.util.List;

public interface IActorService {

    List<Actor> getAllActors();

    Actor getActorById(Integer id);

    Actor createActor(ActorDTO actorDTO);

    Actor updateActor(Integer id, ActorDTO actorDTO);

    void deleteActor(Integer id);

}
