package com.movieshop.server.service;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Film;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.ActorMapper;
import com.movieshop.server.model.ActorDTO;
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
    public List<ActorDTO> getAllActors() {
        List<Actor> actors = actorRepository.findAll();
        return actors.stream().map(actorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ActorDTO getActorById(Integer id) {
        Actor actor = getActorByIdOrElseThrow(id);
        return actorMapper.toDto(actor);
    }

    @Override
    public ActorDTO createActor(ActorDTO actorDTO) {
        List<Integer> filmIds = actorDTO.getFilmIds();
        Set<Film> films;
        if (filmIds != null && !filmIds.isEmpty()) {
            films = filmIds.stream().map(id ->
                    filmRepository.findById(id).orElseThrow(() ->
                            new ResourceNotFoundException("Film not found")))
                    .collect(Collectors.toSet());
        } else {
            films = new HashSet<>();
        }
        Actor actor = actorMapper.toEntity(actorDTO, films);
        Actor savedActor = actorRepository.save(actor);

        return actorMapper.toDto(savedActor);
    }

    @Override
    public ActorDTO updateActor(Integer id, ActorDTO actorDTO) {
        Actor existentActor = getActorByIdOrElseThrow(id);

        existentActor.setFirstName(actorDTO.getFirstName());
        existentActor.setLastName(actorDTO.getLastName());

        Actor updatedActor = actorRepository.save(existentActor);

        return actorMapper.toDto(updatedActor);
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
