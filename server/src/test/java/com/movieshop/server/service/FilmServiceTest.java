package com.movieshop.server.service;

import com.movieshop.server.domain.*;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.FilmMapper;
import com.movieshop.server.model.FilmDTO;
import com.movieshop.server.model.MoviePageResponse;
import com.movieshop.server.repository.ActorRepository;
import com.movieshop.server.repository.CategoryRepository;
import com.movieshop.server.repository.FilmRepository;
import com.movieshop.server.repository.LanguageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FilmServiceImplTest {

    @InjectMocks
    private FilmServiceImpl filmService;

    @Mock
    private FilmRepository filmRepository;
    @Mock
    private FilmMapper filmMapper;
    @Mock
    private LanguageRepository languageRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ActorRepository actorRepository;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    private Film sampleFilm() {
        Film film = new Film();
        film.setId(1);
        film.setTitle("Test Movie");
        film.setDescription("Description");
        film.setReleaseYear(2024);
        film.setLanguage(new Language(1, "English"));
        film.setOriginalLanguage(new Language(2, "French"));
        film.setRentalDuration(5);
        film.setRentalRate(Double.parseDouble("3.99"));
        film.setLength(120);
        film.setReplacementCost(Double.parseDouble("20.00"));
        film.setRating(Rating.valueOf("PG"));
        return film;
    }

    private FilmDTO sampleFilmDTO() {
        return FilmDTO.builder()
                .title("Test Movie")
                .description("Description")
                .releaseYear(2024)
                .language("English")
                .originalLanguage("French")
                .rentalDuration(5)
                .rentalRate(Double.parseDouble("3.99"))
                .length(120)
                .replacementCost(Double.parseDouble("20.00"))
                .rating(Rating.valueOf("PG"))
                .categoryIds(List.of(1))
                .actorIds(List.of(1))
                .build();
    }

    @Test
    void getFilmById_success() {
        Film film = sampleFilm();
        FilmDTO dto = sampleFilmDTO();

        when(filmRepository.findById(1)).thenReturn(Optional.of(film));
        when(filmMapper.toDto(film)).thenReturn(dto);

        FilmDTO result = filmService.getFilmById(1);
        assertThat(result.getTitle()).isEqualTo("Test Movie");
    }

    @Test
    void getFilmById_notFound() {
        when(filmRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmService.getFilmById(1))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getFilmByTitle_success() {
        Film film = sampleFilm();
        FilmDTO dto = sampleFilmDTO();

        when(filmRepository.findByTitle("Test Movie")).thenReturn(Optional.of(film));
        when(filmMapper.toDto(film)).thenReturn(dto);

        FilmDTO result = filmService.getFilmByTitle("Test Movie");
        assertThat(result.getTitle()).isEqualTo("Test Movie");
    }

    @Test
    void getFilmByTitle_notFound() {
        when(filmRepository.findByTitle("Unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmService.getFilmByTitle("Unknown"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllFilms_success() {
        List<Film> films = List.of(sampleFilm());
        when(filmRepository.findAll()).thenReturn(films);
        when(filmMapper.toDto(any())).thenReturn(sampleFilmDTO());

        List<FilmDTO> result = filmService.getAllFilms();
        assertThat(result).hasSize(1);
    }

    @Test
    void getAllFilmsPaginated_success() {
        List<Film> films = List.of(sampleFilm());
        Page<Film> page = new PageImpl<>(films);

        when(filmRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(filmRepository.count()).thenReturn(1L);

        MoviePageResponse result = filmService.getAllFilmsPaginated(0, 10);
        assertThat(result.getFilms().size()).isEqualTo(1);
    }

    @Test
    void getAllFilmsPaginated_exceptionThrown_shouldThrowResourceNotFoundException() {
        int page = 0;
        int limit = 10;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "id"));
        when(filmRepository.findAll(pageable)).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> filmService.getAllFilmsPaginated(page, limit))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Error fetching paginated films");
    }

    @Test
    void createFilm_success() {
        FilmDTO dto = sampleFilmDTO();
        Language lang = new Language(1, "English");
        Language origLang = new Language(2, "French");
        Category category = new Category(1, "Action");
        Actor actor = new Actor(1, "John", "Doe");

        when(languageRepository.findByName("English")).thenReturn(Optional.of(lang));
        when(languageRepository.findByName("French")).thenReturn(Optional.of(origLang));
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(actorRepository.findById(1)).thenReturn(Optional.of(actor));

        Film filmEntity = sampleFilm();
        when(filmMapper.toEntity(eq(dto), eq(lang), eq(origLang), anySet(), anySet())).thenReturn(filmEntity);
        when(filmRepository.save(filmEntity)).thenReturn(filmEntity);
        when(filmMapper.toDto(filmEntity)).thenReturn(dto);

        FilmDTO result = filmService.createFilm(dto);
        assertThat(result.getTitle()).isEqualTo("Test Movie");
    }

    @Test
    void createFilm_withNoCategoriesOrActors_usesEmptySets() {
        Language lang = new Language(1, "English");
        Language origLang = new Language(2, "French");

        FilmDTO dto = FilmDTO.builder()
                .title("Test Movie")
                .description("Description")
                .releaseYear(2024)
                .language("English")
                .originalLanguage("French")
                .rentalDuration(5)
                .rentalRate(3.99)
                .length(120)
                .replacementCost(20.00)
                .rating(Rating.PG)
                .categoryIds(Collections.emptyList())
                .actorIds(Collections.emptyList())
                .build();

        when(languageRepository.findByName("English")).thenReturn(Optional.of(lang));
        when(languageRepository.findByName("French")).thenReturn(Optional.of(origLang));

        Film film = new Film();
        when(filmMapper.toEntity(eq(dto), eq(lang), eq(origLang), anySet(), anySet())).thenReturn(film);
        when(filmRepository.save(film)).thenReturn(film);
        when(filmMapper.toDto(film)).thenReturn(dto);

        filmService.createFilm(dto);

        ArgumentCaptor<Set<Category>> categoriesCaptor = ArgumentCaptor.forClass(Set.class);
        ArgumentCaptor<Set<Actor>> actorsCaptor = ArgumentCaptor.forClass(Set.class);

        verify(filmMapper).toEntity(eq(dto), eq(lang), eq(origLang), categoriesCaptor.capture(), actorsCaptor.capture());

        assertThat(categoriesCaptor.getValue()).isEmpty();
        assertThat(actorsCaptor.getValue()).isEmpty();
    }

    @Test
    void createFilm_languageNotFound_throwsException() {
        FilmDTO dto = FilmDTO.builder()
                .title("Test Movie")
                .description("Description")
                .releaseYear(2024)
                .language("English")
                .originalLanguage("French")
                .rentalDuration(5)
                .rentalRate(3.99)
                .length(120)
                .replacementCost(20.00)
                .rating(Rating.PG)
                .categoryIds(Collections.emptyList())
                .actorIds(Collections.emptyList())
                .build();

        when(languageRepository.findByName("English")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> filmService.createFilm(dto)
        );

        assertThat(exception.getMessage()).isEqualTo("Language not found with name: English");
    }

    @Test
    void createFilm_originalLanguageNotFound_throwsException() {
        Language lang = new Language(1, "English");

        FilmDTO dto = FilmDTO.builder()
                .title("Test Movie")
                .description("Description")
                .releaseYear(2024)
                .language("English")
                .originalLanguage("French")
                .rentalDuration(5)
                .rentalRate(3.99)
                .length(120)
                .replacementCost(20.00)
                .rating(Rating.PG)
                .categoryIds(Collections.emptyList())
                .actorIds(Collections.emptyList())
                .build();

        when(languageRepository.findByName("English")).thenReturn(Optional.of(lang));
        when(languageRepository.findByName("French")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> filmService.createFilm(dto)
        );

        assertThat(exception.getMessage()).isEqualTo("Language not found with name: English");
    }




    @Test
    void updateFilm_success() {
        Film existent = sampleFilm();
        FilmDTO dto = sampleFilmDTO();
        Language lang = new Language(1, "English");
        Language origLang = new Language(2, "French");
        Category category = new Category(1, "Action");
        Actor actor = new Actor(1, "John", "Doe");

        when(filmRepository.findById(1)).thenReturn(Optional.of(existent));
        when(languageRepository.findByName("English")).thenReturn(Optional.of(lang));
        when(languageRepository.findByName("French")).thenReturn(Optional.of(origLang));
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(actorRepository.findById(1)).thenReturn(Optional.of(actor));
        when(filmRepository.save(any())).thenReturn(existent);
        when(filmMapper.toDto(any())).thenReturn(dto);

        FilmDTO result = filmService.updateFilm(1, dto);
        assertThat(result.getTitle()).isEqualTo("Test Movie");
    }

    @Test
    void deleteFilm_success() {
        Film film = sampleFilm();
        when(filmRepository.findById(1)).thenReturn(Optional.of(film));

        filmService.deleteFilm(1);
        verify(filmRepository).delete(film);
    }

    @Test
    void deleteFilm_notFound() {
        when(filmRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmService.deleteFilm(1))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
