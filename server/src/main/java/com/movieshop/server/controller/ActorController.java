package com.movieshop.server.controller;

import com.movieshop.server.model.ActorRequestDTO;
import com.movieshop.server.model.ActorResponseDTO;
import com.movieshop.server.service.IActorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v0/actors")
public class ActorController {

    private final IActorService actorService;

    public ActorController(IActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public ResponseEntity<List<ActorResponseDTO>> getAllActors() {
        return ResponseEntity.ok(actorService.getAllActors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorResponseDTO> getActorById(@PathVariable Integer id) {
        return ResponseEntity.ok(actorService.getActorById(id));
    }

    @PostMapping
    public ResponseEntity<ActorResponseDTO> createActor(@RequestBody ActorRequestDTO actorRequestDTO) {
        ActorResponseDTO createdActor = actorService.createActor(actorRequestDTO);
        URI location = URI.create("/api/v0/actors/" + createdActor.getId());
        return ResponseEntity.created(location).body(createdActor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorResponseDTO> updateActor(@PathVariable Integer id, @RequestBody ActorRequestDTO actorRequestDTO) {
        return ResponseEntity.ok(actorService.updateActor(id, actorRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer id) {
        actorService.deleteActor(id);
        return ResponseEntity.noContent().build();
    }
}
