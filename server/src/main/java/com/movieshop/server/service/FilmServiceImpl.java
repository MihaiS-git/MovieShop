package com.movieshop.server.service;

import com.movieshop.server.domain.Film;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.model.MoviePageResponse;
import com.movieshop.server.repository.FilmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements IFilmService {

    private final FilmRepository filmRepository;

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public Film getFilmById(Long id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public MoviePageResponse findAll(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        try {
            List<Film> films = filmRepository.findAll(pageable).getContent();
            long totalCount = filmRepository.count();

            return new MoviePageResponse(films, totalCount);
        } catch (Exception e) {
            log.error("Error fetching paginated films: {}", e.getMessage());
            throw new ResourceNotFoundException("Error fetching paginated films: " + e.getMessage());
        }
    }
}
