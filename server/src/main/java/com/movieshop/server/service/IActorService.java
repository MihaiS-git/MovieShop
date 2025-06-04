package com.movieshop.server.service;

import com.movieshop.server.model.ActorRequestDTO;
import com.movieshop.server.model.ActorResponseDTO;
import com.movieshop.server.model.ActorResponseForFilmDTO;

import java.util.List;

public interface IActorService {

    List<ActorResponseDTO> getAllActors();

    ActorResponseDTO getActorById(Integer id);

    List<ActorResponseForFilmDTO> searchActorsByName(String name);

    ActorResponseDTO createActor(ActorRequestDTO actorRequestDTO);

    ActorResponseDTO updateActor(Integer id, ActorRequestDTO actorRequestDTO);

    void deleteActor(Integer id);

}
