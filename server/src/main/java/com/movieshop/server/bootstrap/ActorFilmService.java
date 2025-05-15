package com.movieshop.server.bootstrap;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Film;
import com.movieshop.server.repository.ActorRepository;
import com.movieshop.server.repository.FilmRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;

@Service
@RequiredArgsConstructor
public class ActorFilmService {

    private final ActorRepository actorRepository;
    private final FilmRepository filmRepository;

    @Transactional
    public void loadActorFilmData() {
        try(BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/actor_film_data_cleaned.csv"))) {
            String line = reader.readLine();

            while((line = reader.readLine()) != null){
                String[] fields = line.split(",");

                Actor actor = actorRepository.findById(Integer.parseInt(fields[0])).orElse(null);
                Film film = filmRepository.findById(Integer.parseInt(fields[1])).orElse(null);

                if (film != null && actor != null) {
                    film.getActors().add(actor);
                    actor.getFilms().add(film);
                    filmRepository.save(film);
                    actorRepository.save(actor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
