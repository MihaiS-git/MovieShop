package com.movieshop.server.service;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.ActorMapper;
import com.movieshop.server.model.ActorRequestDTO;
import com.movieshop.server.model.ActorResponseDTO;
import com.movieshop.server.repository.ActorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

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
        List<ActorResponseDTO> actorResponseDTOS = List.of(new ActorResponseDTO(), new ActorResponseDTO());

        when(actorRepository.findAll()).thenReturn(actors);
        when(actorMapper.toResponseDto(any(Actor.class))).thenReturn(new ActorResponseDTO());

        List<ActorResponseDTO> result = actorService.getAllActors();

        assertEquals(actorResponseDTOS.size(), result.size());
        verify(actorRepository, times(1)).findAll();
        verify(actorMapper, times(2)).toResponseDto(any(Actor.class));
    }

    @Test
    void getActorById_WhenFound_ShouldReturnDTO() {
        Actor actor = new Actor();
        ActorResponseDTO dto = new ActorResponseDTO();

        when(actorRepository.findById(1)).thenReturn(Optional.of(actor));
        when(actorMapper.toResponseDto(actor)).thenReturn(dto);

        ActorResponseDTO result = actorService.getActorById(1);

        assertNotNull(result);
        verify(actorRepository, times(1)).findById(1);
        verify(actorMapper, times(1)).toResponseDto(actor);
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
    void createActor_ShouldSaveAndReturnDTO() {
        ActorRequestDTO actorRequestDTO = new ActorRequestDTO();

        Actor actorEntity = new Actor();
        Actor savedActor = new Actor();
        ActorResponseDTO savedDto = new ActorResponseDTO();

        when(actorMapper.toEntity(actorRequestDTO)).thenReturn(actorEntity);
        when(actorRepository.save(actorEntity)).thenReturn(savedActor);
        when(actorMapper.toResponseDto(savedActor)).thenReturn(savedDto);

        ActorResponseDTO result = actorService.createActor(actorRequestDTO);

        assertNotNull(result);
        verify(actorMapper).toEntity(actorRequestDTO);
        verify(actorRepository).save(actorEntity);
        verify(actorMapper).toResponseDto(savedActor);
    }

    @Test
    void updateActor_WhenFound_ShouldUpdateAndReturnDTO() {
        Integer id = 1;
        ActorRequestDTO dto = ActorRequestDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Actor existingActor = new Actor();
        existingActor.setFirstName("OldFirstName");
        existingActor.setLastName("OldLastName");

        Actor savedActor = new Actor();
        savedActor.setFirstName("John");
        savedActor.setLastName("Doe");

        ActorResponseDTO updatedDto = ActorResponseDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        when(actorRepository.findById(id)).thenReturn(Optional.of(existingActor));
        when(actorRepository.save(existingActor)).thenReturn(savedActor);
        when(actorMapper.toResponseDto(savedActor)).thenReturn(updatedDto);

        ActorResponseDTO result = actorService.updateActor(id, dto);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(actorRepository).findById(id);
        verify(actorRepository).save(existingActor);
        verify(actorMapper).toResponseDto(savedActor);
    }

    @Test
    void updateActor_WhenNotFound_ShouldThrowException() {
        Integer id = 1;
        ActorRequestDTO dto = new ActorRequestDTO();

        when(actorRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            actorService.updateActor(id, dto);
        });

        assertEquals("Actor not found with id: " + id, exception.getMessage());
        verify(actorRepository, times(1)).findById(id);
        verify(actorRepository, never()).save(any());
        verify(actorMapper, never()).toResponseDto(any());
    }

    @Test
    void deleteActor_WhenFound_ShouldDelete() {
        Integer id = 1;
        Actor actor = new Actor();

        when(actorRepository.findById(id)).thenReturn(Optional.of(actor));
        doNothing().when(actorRepository).deleteById(id);

        assertDoesNotThrow(() -> actorService.deleteActor(id));

        verify(actorRepository).findById(id);
        verify(actorRepository).deleteById(id);
    }

    @Test
    void deleteActor_WhenNotFound_ShouldThrowException() {
        Integer id = 1;

        when(actorRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            actorService.deleteActor(id);
        });

        assertEquals("Actor not found with id: " + id, exception.getMessage());
        verify(actorRepository).findById(id);
        verify(actorRepository, never()).deleteById(any());
    }
}
