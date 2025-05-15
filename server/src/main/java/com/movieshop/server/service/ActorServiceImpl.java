package com.movieshop.server.service;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.ActorMapper;
import com.movieshop.server.model.ActorDTO;
import com.movieshop.server.repository.ActorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorServiceImpl implements IActorService {

    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;

    public ActorServiceImpl(ActorRepository actorRepository, ActorMapper actorMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
    }

    @Override
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    @Override
    public Actor getActorById(Integer id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    }

    @Override
    public Actor createActor(ActorDTO actorDTO) {
        return actorRepository.save(actorMapper.toEntity(actorDTO));
    }

    @Override
    public Actor updateActor(Integer id, ActorDTO actorDTO) {
        Actor existentActor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));

        existentActor.setFirstName(actorDTO.getFirstName());
        existentActor.setLastName(actorDTO.getLastName());

        return actorRepository.save(existentActor);
    }

    @Override
    public void deleteActor(Integer id) {
        Actor existentActor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
        actorRepository.deleteById(id);
    }
}
