package com.movieshop.server.service;

import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Language;
import com.movieshop.server.domain.Rating;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private FilmMapper filmMapper;

    @Mock
    private LanguageServiceImpl languageService;

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
        Film film = Film.builder().id(1).title("Test Movie").build();
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
        Film film = Film.builder().id(2).title("Avatar").build();
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
        List<Film> mockFilms = Arrays.asList(
                Film.builder().id(1).title("A").build(),
                Film.builder().id(2).title("B").build()
        );

        when(filmRepository.findAll()).thenReturn(mockFilms);

        List<Film> result = filmService.getAllFilms();

        assertEquals(2, result.size());
    }

    @Test
    void findAll_ReturnsPaginatedFilms() {
        List<Film> content = Arrays.asList(Film.builder().id(1).title("X").build());
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
        Film film = Film.builder().id(5).title("New Film").build();

        when(filmMapper.toEntity(dto)).thenReturn(film);
        when(filmRepository.save(film)).thenReturn(film);

        Film result = filmService.createFilm(dto);

        assertEquals("New Film", result.getTitle());
    }

    @Test
    void updateFilm_WhenExists_UpdatesAndReturnsFilm() {
        Film existing = Film.builder().id(10).title("Old").build();
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

    @Test
    void updateFilm_WhenNotFound_ThrowsException() {
        when(filmRepository.findById(200)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> filmService.updateFilm(200, buildFilmDTO()));
    }

    @Test
    void deleteFilm_WhenExists_DeletesSuccessfully() {
        Film film = Film.builder().id(30).build();
        when(filmRepository.findById(30)).thenReturn(Optional.of(film));

        filmService.deleteFilm(30);

        verify(filmRepository).delete(film);
    }

    @Test
    void deleteFilm_WhenNotFound_ThrowsException() {
        when(filmRepository.findById(300)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> filmService.deleteFilm(300));
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
                .lastUpdate(LocalDateTime.now())
                .build();
    }
}
