package com.movieshop.server.service;

import com.movieshop.server.model.ActorRequestDTO;
import com.movieshop.server.model.ActorResponseDTO;

import java.util.List;

public interface IActorService {

    List<ActorResponseDTO> getAllActors();

    ActorResponseDTO getActorById(Integer id);

    ActorResponseDTO createActor(ActorRequestDTO actorRequestDTO);

    ActorResponseDTO updateActor(Integer id, ActorRequestDTO actorRequestDTO);

    void deleteActor(Integer id);

}
