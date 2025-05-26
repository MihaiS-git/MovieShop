package com.movieshop.server.service;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Category;
import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Language;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.FilmMapper;
import com.movieshop.server.model.FilmRequestDTO;
import com.movieshop.server.model.FilmResponseDTO;
import com.movieshop.server.model.MoviePageResponse;
import com.movieshop.server.repository.CategoryRepository;
import com.movieshop.server.repository.FilmRepository;
import com.movieshop.server.repository.LanguageRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmServiceImpl implements IFilmService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final LanguageRepository languageRepository;

    public FilmServiceImpl(
            FilmRepository filmRepository,
            FilmMapper filmMapper,
            LanguageRepository languageRepository
    ) {
        this.filmRepository = filmRepository;
        this.filmMapper = filmMapper;
        this.languageRepository = languageRepository;
    }

    @Override
    public FilmResponseDTO getFilmById(Integer id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        return filmMapper.toResponseDto(film);
    }

    @Override
    public FilmResponseDTO getFilmByTitle(String title) {
        Film film = filmRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with title: " + title));

        return filmMapper.toResponseDto(film);
    }

    @Override
    public List<FilmResponseDTO> getAllFilms() {
        List<Film> films = filmRepository.findAll();
        return films.stream().map(filmMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MoviePageResponse getAllFilmsPaginated(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "id"));
        try {
            List<Film> films = filmRepository.findAll(pageable).getContent();
            List<FilmResponseDTO> filmDTOs = films.stream()
                    .map(filmMapper::toResponseDto)
                    .toList();
            long totalCount = filmRepository.count();

            return new MoviePageResponse(filmDTOs, totalCount);
        } catch (Exception e) {
            log.error("Error fetching paginated films: {}", e.getMessage());
            throw new ResourceNotFoundException("Error fetching paginated films: " + e.getMessage());
        }
    }

    @Override
    public FilmResponseDTO createFilm(FilmRequestDTO filmRequestDTO) {
        Language language = languageRepository.findByName(filmRequestDTO.getLanguage()).orElseThrow(() ->
                new ResourceNotFoundException("Language not found with name: " + filmRequestDTO.getLanguage()));
        Language originalLanguage = languageRepository.findByName(filmRequestDTO.getOriginalLanguage()).orElseThrow(() ->
                new ResourceNotFoundException("Language not found with name: " + filmRequestDTO.getLanguage()));

        Film film = filmMapper.toEntity(filmRequestDTO);
        film.setLanguage(language);
        film.setOriginalLanguage(originalLanguage);

        Film savedFilm = filmRepository.save(film);

        return filmMapper.toResponseDto(savedFilm);
    }

    @Override
    public FilmResponseDTO updateFilm(Integer id, FilmRequestDTO filmRequestDTO) {
        Film existentFilm = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        existentFilm.setTitle(filmRequestDTO.getTitle());
        existentFilm.setDescription(filmRequestDTO.getDescription());
        existentFilm.setReleaseYear(filmRequestDTO.getReleaseYear());
        existentFilm.setLanguage(languageRepository.findByName(filmRequestDTO.getLanguage())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with name: " + filmRequestDTO.getLanguage())));
        existentFilm.setOriginalLanguage(languageRepository.findByName(filmRequestDTO.getOriginalLanguage())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with name: " + filmRequestDTO.getLanguage())));
        existentFilm.setRentalDuration(filmRequestDTO.getRentalDuration());
        existentFilm.setRentalRate(filmRequestDTO.getRentalRate());
        existentFilm.setLength(filmRequestDTO.getLength());
        existentFilm.setReplacementCost(filmRequestDTO.getReplacementCost());
        existentFilm.setRating(filmRequestDTO.getRating());

        Film updatedFilm = filmRepository.save(existentFilm);

        return filmMapper.toResponseDto(updatedFilm);
    }

    @Override
    public void deleteFilm(Integer id) {
        Film existentFilm = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        filmRepository.delete(existentFilm);
    }
}

