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
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    @Override
    public Actor getActorById(Integer id) {
        return getActorByIdOrElseThrow(id);
    }

    @Override
    public Actor createActor(ActorDTO actorDTO) {
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

        return actorRepository.save(actor);
    }

    @Override
    public Actor updateActor(Integer id, ActorDTO actorDTO) {
        Actor existentActor = getActorByIdOrElseThrow(id);

        existentActor.setFirstName(actorDTO.getFirstName());
        existentActor.setLastName(actorDTO.getLastName());

        return actorRepository.save(existentActor);
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
