package com.movieshop.server.service;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Film;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.ActorMapper;
import com.movieshop.server.model.ActorResponseDTO;
import com.movieshop.server.repository.ActorRepository;
import com.movieshop.server.repository.FilmRepository;
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
    private FilmRepository filmRepository;

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
        ActorResponseDTO actorResponseDTO = new ActorResponseDTO();

        Actor actorEntity = new Actor();
        Actor savedActor = new Actor();
        ActorResponseDTO savedDto = new ActorResponseDTO();

        when(actorMapper.toEntity(actorResponseDTO, new HashSet<>())).thenReturn(actorEntity);
        when(actorRepository.save(actorEntity)).thenReturn(savedActor);
        when(actorMapper.toResponseDto(savedActor)).thenReturn(savedDto);

        ActorResponseDTO result = actorService.createActor(actorResponseDTO);

        assertNotNull(result);
        verify(actorMapper).toEntity(actorResponseDTO, new HashSet<>());
        verify(actorRepository).save(actorEntity);
        verify(actorMapper).toResponseDto(savedActor);
    }

    @Test
    void createActor_WithFilmIds_ShouldMapFilmsAndSave() {
        // Arrange
        Integer filmId1 = 1;
        Integer filmId2 = 2;

        ActorResponseDTO actorResponseDTO = ActorResponseDTO.builder()
                .filmIds(List.of(filmId1, filmId2))
                .build();

        Film film1 = new Film();
        film1.setId(1);
        Film film2 = new Film();
        film2.setId(2);

        Actor actorEntity = new Actor();
        Actor savedActor = new Actor();
        ActorResponseDTO savedDto = new ActorResponseDTO();

        when(filmRepository.findById(filmId1)).thenReturn(Optional.of(film1));
        when(filmRepository.findById(filmId2)).thenReturn(Optional.of(film2));
        when(actorMapper.toEntity(actorResponseDTO, Set.of(film1, film2))).thenReturn(actorEntity);
        when(actorRepository.save(actorEntity)).thenReturn(savedActor);
        when(actorMapper.toResponseDto(savedActor)).thenReturn(savedDto);

        // Act
        ActorResponseDTO result = actorService.createActor(actorResponseDTO);

        // Assert
        assertNotNull(result);
        verify(filmRepository).findById(filmId1);
        verify(filmRepository).findById(filmId2);
        verify(actorMapper).toEntity(actorResponseDTO, Set.of(film1, film2));
        verify(actorRepository).save(actorEntity);
        verify(actorMapper).toResponseDto(savedActor);
    }


    @Test
    void updateActor_WhenFound_ShouldUpdateAndReturnDTO() {
        Integer id = 1;
        ActorResponseDTO dto = ActorResponseDTO.builder()
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
        ActorResponseDTO dto = new ActorResponseDTO();

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
