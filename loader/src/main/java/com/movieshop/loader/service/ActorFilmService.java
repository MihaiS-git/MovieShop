package com.movieshop.loader.service;

import com.movieshop.loader.domain.Actor;
import com.movieshop.loader.domain.Film;
import com.movieshop.loader.repository.ActorRepository;
import com.movieshop.loader.repository.FilmRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class ActorFilmService {

    private final ActorRepository actorRepository;
    private final FilmRepository filmRepository;

    @Transactional
    public void loadActorFilmData() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/actor_film.csv")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("CSV file not found on classpath: data/actor_film.com.movieshop.loader.csv");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line = reader.readLine();

                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");

                    int actorId = Integer.parseInt(fields[0]);
                    int filmId = Integer.parseInt(fields[1]);

                    Actor actor = actorRepository.findById(actorId).orElse(null);
                    Film film = filmRepository.findById(filmId).orElse(null);

                    if (film != null && actor != null) {
                        if (!film.getActors().contains(actor)) {
                            film.addActor(actor);
                        }
                    }
                }
                filmRepository.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading actor-film data", e);
        }
    }
}
