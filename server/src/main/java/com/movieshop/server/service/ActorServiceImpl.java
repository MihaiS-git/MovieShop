package com.movieshop.server.service;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Film;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.ActorMapper;
import com.movieshop.server.model.ActorRequestDTO;
import com.movieshop.server.model.ActorResponseDTO;
import com.movieshop.server.model.ActorResponseForFilmDTO;
import com.movieshop.server.repository.ActorRepository;
import com.movieshop.server.repository.FilmRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActorServiceImpl implements IActorService {

    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;
    private final FilmRepository filmRepository;

    public ActorServiceImpl(
            ActorRepository actorRepository,
            ActorMapper actorMapper,
            FilmRepository filmRepository
    ) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
        this.filmRepository = filmRepository;
    }

    @Override
    public List<ActorResponseDTO> getAllActors() {
        List<Actor> actors = actorRepository.findAll();
        return actors.stream().map(actorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ActorResponseDTO getActorById(Integer id) {
        Actor actor = getActorByIdOrElseThrow(id);
        return actorMapper.toResponseDto(actor);
    }

    @Override
    public List<ActorResponseForFilmDTO> searchActorsByName(String searchName) {
        List<Actor> actors = actorRepository.searchByName(searchName);
        if (actors.isEmpty()) {
            throw new ResourceNotFoundException("No actors found with name: " + searchName);
        }
        List<ActorResponseForFilmDTO> actorResponses = actors.stream()
                .limit(3)
                .map(actorMapper::toFilmResponseDto)
                .toList();
         return actorResponses;
    }

    @Override
    public ActorResponseDTO createActor(ActorRequestDTO actorRequestDTO) {
        Actor actor = actorMapper.toEntity(actorRequestDTO);
        Actor savedActor = actorRepository.save(actor);

        return actorMapper.toResponseDto(savedActor);
    }

    @Override
    public ActorResponseDTO updateActor(Integer id, ActorRequestDTO actorRequestDTO) {
        Actor existentActor = getActorByIdOrElseThrow(id);

        existentActor.setFirstName(actorRequestDTO.getFirstName());
        existentActor.setLastName(actorRequestDTO.getLastName());

        Actor updatedActor = actorRepository.save(existentActor);

        return actorMapper.toResponseDto(updatedActor);
    }

    @Override
    public void deleteActor(Integer id) {
        Actor existentActor = getActorByIdOrElseThrow(id);
        actorRepository.deleteById(id);
    }

    private Actor getActorByIdOrElseThrow(Integer id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    }
}
