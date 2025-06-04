package com.movieshop.server.service;

import com.movieshop.server.domain.*;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.FilmMapper;
import com.movieshop.server.model.*;
import com.movieshop.server.repository.ActorRepository;
import com.movieshop.server.repository.CategoryRepository;
import com.movieshop.server.repository.FilmRepository;
import com.movieshop.server.repository.LanguageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilmServiceImplTest {

    @InjectMocks
    private FilmServiceImpl filmService;

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private FilmMapper filmMapper;

    @Mock
    private LanguageRepository languageRepository;

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
        Category category1 = new Category(1, "A");
        Actor actor1 = new Actor(1, "John", "Doe");

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
        film.setCategories(new HashSet<>(List.of(category1)));
        film.setActors(new HashSet<>(List.of(actor1)));
        return film;
    }

    private FilmResponseDTO sampleFilmDTO() {
        return FilmResponseDTO.builder()
                .id(1)
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
                .build();
    }

    private FilmFullResponseDTO sampleFullFilmDTO() {
        return FilmFullResponseDTO.builder()
                .id(1)
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
                .actors(List.of(
                        ActorResponseForFilmDTO.builder()
                                .id(1)
                                .firstName("John")
                                .lastName("Doe")
                                .build()
                ))
                .build();
    }

    private FilmUpdateResponseDTO sampleUpdateFilmDTO() {
        return FilmUpdateResponseDTO.builder()
                .id(1)
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
                .build();
    }

    private FilmRequestDTO sampleRequestFilmDTO() {
        return FilmRequestDTO.builder()
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
                .build();
    }

    @Test
    void getFilmById_success() {
        Film film = sampleFilm();
        FilmFullResponseDTO dto = sampleFullFilmDTO();

        when(filmRepository.findByIdWithAllRelations(1)).thenReturn(Optional.of(film));
        when(filmMapper.toFullResponseDto(film)).thenReturn(dto);

        FilmFullResponseDTO result = filmService.getFilmById(1);
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
        FilmResponseDTO dto = sampleFilmDTO();

        when(filmRepository.findByTitle("Test Movie")).thenReturn(Optional.of(film));
        when(filmMapper.toResponseDto(film)).thenReturn(dto);

        FilmResponseDTO result = filmService.getFilmByTitle("Test Movie");
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
        when(filmMapper.toResponseDto(any())).thenReturn(sampleFilmDTO());

        List<FilmResponseDTO> result = filmService.getAllFilms();
        assertThat(result).hasSize(1);
    }

    @Test
    void createFilm_success() {
        FilmRequestDTO dto = sampleRequestFilmDTO();
        Language lang = new Language(1, "English");
        Language origLang = new Language(2, "French");

        FilmResponseDTO responseDto = FilmResponseDTO.builder()
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
                .build();


        when(languageRepository.findByNameIgnoreCase("English")).thenReturn(Optional.of(lang));
        when(languageRepository.findByNameIgnoreCase("French")).thenReturn(Optional.of(origLang));

        Film filmEntity = sampleFilm();
        when(filmMapper.toEntity(dto)).thenReturn(filmEntity);
        when(filmRepository.save(filmEntity)).thenReturn(filmEntity);
        when(filmMapper.toResponseDto(filmEntity)).thenReturn(responseDto);

        FilmResponseDTO result = filmService.createFilm(dto);
        assertThat(result.getTitle()).isEqualTo("Test Movie");
    }

    @Test
    void createFilm_languageNotFound_throwsException() {
        FilmRequestDTO dto = FilmRequestDTO.builder()
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
                .build();

        when(languageRepository.findByNameIgnoreCase("English")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> filmService.createFilm(dto)
        );

        assertThat(exception.getMessage()).isEqualTo("Language not found with name: English");
    }

    @Test
    void createFilm_originalLanguageNotFound_throwsException() {
        Language lang = new Language(1, "English");

        FilmRequestDTO dto = FilmRequestDTO.builder()
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
                .build();

        when(languageRepository.findByNameIgnoreCase("English")).thenReturn(Optional.of(lang));
        when(languageRepository.findByNameIgnoreCase("French")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> filmService.createFilm(dto)
        );

        assertThat(exception.getMessage()).isEqualTo("Language not found with name: English");
    }

    @Test
    void updateFilm_success() {
        Film existent = sampleFilm();
        FilmRequestDTO requestDto = sampleRequestFilmDTO();

        Language lang = new Language(1, "English");
        Language origLang = new Language(2, "French");

        Film updatedFilm = sampleFilm();
        FilmUpdateResponseDTO dto = sampleUpdateFilmDTO();

        when(filmRepository.findByIdWithBasicFields(1)).thenReturn(Optional.of(existent));
        when(languageRepository.findByNameIgnoreCase("English")).thenReturn(Optional.of(lang));
        when(languageRepository.findByNameIgnoreCase("French")).thenReturn(Optional.of(origLang));
        when(filmMapper.toEntity(requestDto)).thenReturn(updatedFilm);
        when(filmRepository.save(any(Film.class))).thenReturn(updatedFilm);
        when(filmMapper.toUpdateResponseDto(updatedFilm)).thenReturn(dto);

        FilmUpdateResponseDTO result = filmService.updateFilm(1, requestDto);

        assertThat(result.getTitle()).isEqualTo("Test Movie");
        assertThat(result.getLanguage()).isEqualTo("English");
        assertThat(result.getOriginalLanguage()).isEqualTo("French");
    }

    @Test
    void addCategoryToFilm_success() {
        Film film = sampleFilm();
        Category category = new Category();
        category.setName("Action");

        FilmFullResponseDTO dto = sampleFullFilmDTO();

        when(filmRepository.findByIdWithAllRelations(1)).thenReturn(Optional.of(film));
        when(categoryRepository.findByNameIgnoreCase("Action")).thenReturn(Optional.of(category));
        when(filmRepository.save(film)).thenReturn(film);
        when(filmMapper.toFullResponseDto(film)).thenReturn(dto);

        AddCategoryToFilmRequestDTO categoryDto = new AddCategoryToFilmRequestDTO("Action");

        FilmFullResponseDTO result = filmService.addCategoryToFilm(1, categoryDto);

        verify(filmRepository).save(film);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void addCategoryToFilm_filmNotFound_throws() {
        when(filmRepository.findById(1)).thenReturn(Optional.empty());
        AddCategoryToFilmRequestDTO categoryDto = new AddCategoryToFilmRequestDTO("Action");

        assertThatThrownBy(() -> filmService.addCategoryToFilm(1, categoryDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Film not found");
    }

    @Test
    void addCategoryToFilm_categoryNotFound_throws() {
        Film film = sampleFilm();
        when(filmRepository.findByIdWithAllRelations(1)).thenReturn(Optional.of(film));
        when(categoryRepository.findByNameIgnoreCase("Action")).thenReturn(Optional.empty());
        AddCategoryToFilmRequestDTO categoryDto = new AddCategoryToFilmRequestDTO();

        assertThatThrownBy(() -> filmService.addCategoryToFilm(1, categoryDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found");
    }

    @Test
    void removeCategoryFromFilm_success() {
        Film film = sampleFilm();
        Category category = new Category();
        category.setName("Action");
        film.getCategories().add(category);

        FilmFullResponseDTO dto = sampleFullFilmDTO();

        when(filmRepository.findByIdWithAllRelations(1)).thenReturn(Optional.of(film));
        when(categoryRepository.findByNameIgnoreCase("Action")).thenReturn(Optional.of(category));
        when(filmRepository.save(film)).thenReturn(film);
        when(filmMapper.toFullResponseDto(film)).thenReturn(dto);

        FilmFullResponseDTO result = filmService.removeCategoryFromFilm(1, "Action");

        verify(filmRepository).save(film);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void removeCategoryFromFilm_categoryNotInFilm_throws() {
        Film film = sampleFilm();
        Category category = new Category();
        category.setName("Action");

        when(filmRepository.findByIdWithAllRelations(1)).thenReturn(Optional.of(film));
        when(categoryRepository.findByNameIgnoreCase("Action")).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> filmService.removeCategoryFromFilm(1, "Action"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found in film");
    }

    @Test
    void addActorToFilm_success() {
        Film film = sampleFilm();
        Actor actor = new Actor();
        actor.setId(1);

        FilmFullResponseDTO dto = sampleFullFilmDTO();

        when(filmRepository.findByIdWithAllRelations(1)).thenReturn(Optional.of(film));
        when(actorRepository.findById(1)).thenReturn(Optional.of(actor));
        when(filmRepository.save(film)).thenReturn(film);
        when(filmMapper.toFullResponseDto(film)).thenReturn(dto);
        AddActorToFilmRequestDTO actorDto = new AddActorToFilmRequestDTO(1);

        FilmFullResponseDTO result = filmService.addActorToFilm(1, actorDto);

        verify(filmRepository).save(film);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void addActorToFilm_filmNotFound_throws() {
        when(filmRepository.findById(1)).thenReturn(Optional.empty());
        AddActorToFilmRequestDTO actorDto = new AddActorToFilmRequestDTO(1);

        assertThatThrownBy(() -> filmService.addActorToFilm(1, actorDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Film not found");
    }

    @Test
    void addActorToFilm_actorNotFound_throws() {
        Film film = sampleFilm();
        when(filmRepository.findByIdWithAllRelations(1)).thenReturn(Optional.of(film));
        when(actorRepository.findById(1)).thenReturn(Optional.empty());
        AddActorToFilmRequestDTO actorDto = new AddActorToFilmRequestDTO(1);


        assertThatThrownBy(() -> filmService.addActorToFilm(1, actorDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Actor not found");
    }

    @Test
    void removeActorFromFilm_success() {
        Film film = sampleFilm();
        Actor actor = new Actor();
        actor.setId(1);
        film.getActors().add(actor);

        FilmFullResponseDTO dto = sampleFullFilmDTO();

        when(filmRepository.findByIdWithAllRelations(1)).thenReturn(Optional.of(film));
        when(actorRepository.findById(1)).thenReturn(Optional.of(actor));
        when(filmRepository.save(film)).thenReturn(film);
        when(filmMapper.toFullResponseDto(film)).thenReturn(dto);

        FilmFullResponseDTO result = filmService.removeActorFromFilm(1, 1);

        verify(filmRepository).save(film);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void removeActorFromFilm_actorNotInFilm_throws() {
        Film film = sampleFilm();
        Actor actor = new Actor();
        actor.setId(1);

        when(filmRepository.findByIdWithAllRelations(1)).thenReturn(Optional.of(film));
        when(actorRepository.findById(1)).thenReturn(Optional.of(actor));

        assertThatThrownBy(() -> filmService.removeActorFromFilm(1, 1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Actor not found in film");
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
