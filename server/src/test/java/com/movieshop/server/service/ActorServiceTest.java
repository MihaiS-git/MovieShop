package com.movieshop.server.service;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Film;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.ActorMapper;
import com.movieshop.server.model.ActorDTO;
import com.movieshop.server.repository.ActorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private ActorMapper actorMapper;

    @InjectMocks
    private ActorServiceImpl actorService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testGetAllActors_shouldReturnAllActors() {
        List<Actor> actors = List.of(new Actor(), new Actor());
        when(actorRepository.findAll()).thenReturn(actors);

        List<Actor> result = actorService.getAllActors();
        assertEquals(2, result.size());
        verify(actorRepository, times(1)).findAll();
    }

    @Test
    void getActorById_WhenFound_ShouldReturnActor() {
        Actor actor = new Actor();
        when(actorRepository.findById(1)).thenReturn(Optional.of(actor));

        Actor result = actorService.getActorById(1);

        assertNotNull(result);
        verify(actorRepository, times(1)).findById(1);
    }

    @Test
    void getActorById_WhenNotFound_ShouldThrowException() {
        when(actorRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            actorService.getActorById(1);
        });

        assertEquals("Actor not found with id: 1", exception.getMessage());
        verify(actorRepository, times(1)).findById(1);
    }

    @Test
    void createActor_ShouldSaveAndReturnActor() {
        ActorDTO actorDTO = new ActorDTO();
        Actor actorEntity = new Actor();
        Actor savedActor = new Actor();

        Set<Film> films = new HashSet<>();

        when(actorMapper.toEntity(actorDTO, films)).thenReturn(actorEntity);
        when(actorRepository.save(actorEntity)).thenReturn(savedActor);

        Actor result = actorService.createActor(actorDTO);

        assertNotNull(result);
//        assertEquals(savedActor, result);
        verify(actorMapper, times(1)).toEntity(actorDTO, films);
        verify(actorRepository, times(1)).save(actorEntity);
    }

    @Test
    void updateActor_WhenFound_ShouldUpdateAndReturnActor() {
        Integer id = 1;
        ActorDTO dto = ActorDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Actor existingActor = new Actor();
                existingActor.setFirstName("OldFirstName");
                existingActor.setLastName("OldLastName");

        Actor savedActor = new Actor();
                savedActor.setFirstName("John");
                savedActor.setLastName("Doe");

        when(actorRepository.findById(id)).thenReturn(Optional.of(existingActor));
        when(actorRepository.save(existingActor)).thenReturn(savedActor);

        Actor result = actorService.updateActor(id, dto);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(actorRepository, times(1)).findById(id);
        verify(actorRepository, times(1)).save(existingActor);
    }

    @Test
    void updateActor_WhenNotFound_ShouldThrowException() {
        Integer id = 1;
        ActorDTO dto = new ActorDTO();

        when(actorRepository.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            actorService.updateActor(id, dto);
        });

        assertEquals("Actor not found with id: " + id, exception.getMessage());
        verify(actorRepository, times(1)).findById(id);
        verify(actorRepository, never()).save(any());
    }

    @Test
    void deleteActor_WhenFound_ShouldDelete() {
        Integer id = 1;
        Actor actor = new Actor();

        when(actorRepository.findById(id)).thenReturn(Optional.of(actor));
        doNothing().when(actorRepository).deleteById(id);

        assertDoesNotThrow(() -> actorService.deleteActor(id));

        verify(actorRepository, times(1)).findById(id);
        verify(actorRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteActor_WhenNotFound_ShouldThrowException() {
        Integer id = 1;

        when(actorRepository.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            actorService.deleteActor(id);
        });

        assertEquals("Actor not found with id: " + id, exception.getMessage());
        verify(actorRepository, times(1)).findById(id);
        verify(actorRepository, never()).deleteById(any());
    }

}
