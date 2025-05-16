package com.movieshop.server.service;

import com.movieshop.server.domain.*;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.FilmMapper;
import com.movieshop.server.model.FilmDTO;
import com.movieshop.server.model.MoviePageResponse;
import com.movieshop.server.repository.FilmRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private FilmMapper filmMapper;

    @Mock
    private LanguageServiceImpl languageService;

    @Mock
    private CategoryServiceImpl categoryService;

    @Mock
    private ActorServiceImpl actorService;

    @InjectMocks
    private FilmServiceImpl filmService;

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
    void getFilmById_WhenExists_ReturnsFilm() {
        Film film = new Film();
        film.setId(1);
        film.setTitle("Test Movie");
        when(filmRepository.findById(1)).thenReturn(Optional.of(film));

        Film result = filmService.getFilmById(1);

        assertEquals("Test Movie", result.getTitle());
    }

    @Test
    void getFilmById_WhenNotFound_ThrowsException() {
        when(filmRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> filmService.getFilmById(999));
    }

    @Test
    void getFilmByTitle_WhenExists_ReturnsFilm() {
        Film film = new Film();
        film.setId(2);
        film.setTitle("Avatar");
        when(filmRepository.findByTitle("Avatar")).thenReturn(Optional.of(film));

        Film result = filmService.getFilmByTitle("Avatar");

        assertEquals(2, result.getId());
    }

    @Test
    void getFilmByTitle_WhenNotFound_ThrowsException() {
        when(filmRepository.findByTitle("NonExistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> filmService.getFilmByTitle("NonExistent"));
    }

    @Test
    void getAllFilms_ReturnsList() {
        Film film1 = new Film();
        film1.setId(1);
        film1.setTitle("A");
        Film film2 = new Film();
        film2.setId(2);

        List<Film> mockFilms = Arrays.asList(film1, film2);

        when(filmRepository.findAll()).thenReturn(mockFilms);

        List<Film> result = filmService.getAllFilms();

        assertEquals(2, result.size());
    }

    @Test
    void findAll_ReturnsPaginatedFilms() {
        Film film1 = new Film();
        film1.setId(1);
        film1.setTitle("X");
        List<Film> content = Arrays.asList(film1);
        Page<Film> page = new PageImpl<>(content);
        when(filmRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(filmRepository.count()).thenReturn(1L);

        MoviePageResponse result = filmService.findAll(0, 10);

        assertEquals(1, result.getFilms().size());
        assertEquals(1L, result.getTotalCount());
    }

    @Test
    void findAll_WhenErrorOccurs_ThrowsException() {
        when(filmRepository.findAll(any(Pageable.class))).thenThrow(new RuntimeException("DB failed"));

        assertThrows(ResourceNotFoundException.class, () -> filmService.findAll(0, 10));
    }

    @Test
    void createFilm_SavesAndReturnsFilm() {
        FilmDTO dto = buildFilmDTO();

        Language language = new Language();
        Language originalLanguage = new Language();

        // Create unique categories with distinct IDs and names
        Category category1 = new Category();
        category1.setId(1);
        category1.setName("Category One");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Category Two");

        // Create unique actors with distinct IDs and names
        Actor actor1 = new Actor();
        actor1.setId(1);
        actor1.setFirstName("FOne");
        actor1.setLastName("LOne");

        Actor actor2 = new Actor();
        actor2.setId(2);
        actor2.setFirstName("FTwo");
        actor2.setLastName("LTwo");

        Set<Category> categories = Set.of(category1, category2);
        Set<Actor> actors = Set.of(actor1, actor2);

        Film film = new Film();
        film.setId(5);
        film.setTitle("New Film");
        film.setLanguage(language);
        film.setOriginalLanguage(originalLanguage);
        film.setCategories(categories);
        film.setActors(actors);

        // Mock dependencies
        when(languageService.getLanguageByName(dto.getLanguage())).thenReturn(language);
        when(languageService.getLanguageByName(dto.getOriginalLanguage())).thenReturn(originalLanguage);

        when(categoryService.getCategoryById(1)).thenReturn(category1);
        when(categoryService.getCategoryById(2)).thenReturn(category2);

        when(actorService.getActorById(1)).thenReturn(actor1);
        when(actorService.getActorById(2)).thenReturn(actor2);

        when(filmMapper.toEntity(dto, language, originalLanguage, categories, actors)).thenReturn(film);
        when(filmRepository.save(film)).thenReturn(film);

        Film result = filmService.createFilm(dto);

        assertEquals("New Film", result.getTitle());
    }




    @Test
    void updateFilm_WhenExists_UpdatesAndReturnsFilm() {
        Film existing = new Film();
        existing.setId(10);
        existing.setTitle("Old");
        FilmDTO dto = buildFilmDTO();

        Language language = Language.builder().id(1).name("English").build();
        Language originalLanguage = Language.builder().id(2).name("Spanish").build();

        when(filmRepository.findById(10)).thenReturn(Optional.of(existing));
        when(languageService.getLanguageByName("English")).thenReturn(language);
        when(languageService.getLanguageByName("Spanish")).thenReturn(originalLanguage);
        when(filmRepository.save(existing)).thenReturn(existing);

        Film result = filmService.updateFilm(10, dto);

        assertEquals("Updated Title", result.getTitle());
        verify(filmRepository).save(existing);
    }

    private FilmDTO buildFilmDTO() {
        return FilmDTO.builder()
                .title("Updated Title")
                .description("Updated Description")
                .releaseYear(2024)
                .language("English")
                .originalLanguage("Spanish")
                .rentalDuration(5)
                .rentalRate(Double.parseDouble("2.99"))
                .length(120)
                .replacementCost(Double.parseDouble("20.00"))
                .rating(Rating.fromString("PG"))
                .categoryIds(List.of(1, 2))
                .actorIds(List.of(1, 2))
                .build();
    }

    @Test
    void updateFilm_WhenNotFound_ThrowsException() {
        when(filmRepository.findById(200)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> filmService.updateFilm(200, buildFilmDTO()));
    }

    @Test
    void deleteFilm_WhenExists_DeletesSuccessfully() {
        Film film = new Film();
        film.setId(30);
        when(filmRepository.findById(30)).thenReturn(Optional.of(film));

        filmService.deleteFilm(30);

        verify(filmRepository).delete(film);
    }

    @Test
    void deleteFilm_WhenNotFound_ThrowsException() {
        when(filmRepository.findById(300)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> filmService.deleteFilm(300));
    }
}
