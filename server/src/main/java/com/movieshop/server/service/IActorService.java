package com.movieshop.server.service;

import com.movieshop.server.model.ActorResponseDTO;

import java.util.List;

public interface IActorService {

    List<ActorResponseDTO> getAllActors();

    ActorResponseDTO getActorById(Integer id);

    ActorResponseDTO createActor(ActorResponseDTO actorResponseDTO);

    ActorResponseDTO updateActor(Integer id, ActorResponseDTO actorResponseDTO);

    void deleteActor(Integer id);

}
