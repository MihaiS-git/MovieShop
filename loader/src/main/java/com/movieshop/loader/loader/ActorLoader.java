package com.movieshop.loader.loader;

import com.movieshop.loader.csv.ActorCsv;
import com.movieshop.loader.utils.CsvReaderUtil;
import com.movieshop.loader.domain.Actor;
import com.movieshop.loader.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ActorLoader {

    private final ActorRepository actorRepository;

    public void load() {
        if (actorRepository.count() > 0) {
            return;
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/actor.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV file not found on classpath: data/actor.com.movieshop.loader.csv");
        }
        List<ActorCsv> actorCsvList = CsvReaderUtil.readCsv(inputStream, ActorCsv.class);

        List<Actor> actors = new ArrayList<>();

        for(ActorCsv actorCsv : actorCsvList) {
            Actor actor = new Actor();
            actor.setFirstName(actorCsv.getFirstName());
            actor.setLastName(actorCsv.getLastName());
            actors.add(actor);
        }

        actorRepository.saveAll(actors);
    }
}
