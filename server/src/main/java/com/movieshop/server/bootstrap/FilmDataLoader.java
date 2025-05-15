package com.movieshop.server.bootstrap;

import com.movieshop.server.domain.*;
import com.movieshop.server.repository.ActorRepository;
import com.movieshop.server.repository.CategoryRepository;
import com.movieshop.server.repository.FilmRepository;
import com.movieshop.server.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDataLoader implements CommandLineRunner {

    private final FilmRepository filmRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;
    private final ActorRepository actorRepository;
    private final FilmCategoryService filmCategoryService;
    private final ActorFilmService actorFilmService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Loading initial data...");
        if(languageRepository.count() == 0) {
            loadInitialLanguageData();
        }
        if(actorRepository.count() == 0) {
            loadInitialActorData();
        }
        if(categoryRepository.count() == 0) {
            loadInitialCategoryData();
        }
        if(filmRepository.count() == 0) {
            loadInitialFilmData();
            filmCategoryService.loadFilmCategoryData();
            actorFilmService.loadActorFilmData();
        }
        log.info("Initial data loaded successfully.");
    }

    private void loadInitialLanguageData() {
        languageRepository.save(new Language("English"));
        languageRepository.save(new Language("French"));
        languageRepository.save(new Language("German"));
        languageRepository.save(new Language("Italian"));
        languageRepository.save(new Language("Japanese"));
        languageRepository.save(new Language("Mandarin"));
        languageRepository.save(new Language("Spanish"));
    }

    private void loadInitialCategoryData(){
        categoryRepository.save(new Category("Action"));
        categoryRepository.save(new Category("Animation"));
        categoryRepository.save(new Category("Children"));
        categoryRepository.save(new Category("Classics"));
        categoryRepository.save(new Category("Comedy"));
        categoryRepository.save(new Category("Documentary"));
        categoryRepository.save(new Category("Drama"));
        categoryRepository.save(new Category("Family"));
        categoryRepository.save(new Category("Foreign"));
        categoryRepository.save(new Category("Games"));
        categoryRepository.save(new Category("Horror"));
        categoryRepository.save(new Category("Music"));
        categoryRepository.save(new Category("New"));
        categoryRepository.save(new Category("Sci-Fi"));
        categoryRepository.save(new Category("Sports"));
        categoryRepository.save(new Category("Travel"));
    }

    private String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (char c : line.toCharArray()){
            if(c == '"'){
                inQuotes = !inQuotes;
            } else if(c == ',' && !inQuotes){
                tokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        tokens.add(sb.toString().trim());
        return tokens.toArray(new String[0]);
    }

    private void loadInitialFilmData(){
        try(BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/film_data_cleaned.csv"))) {
            String line = reader.readLine();

            while((line = reader.readLine()) != null){
                String[] fields = parseCsvLine(line);

                Language originalLanguage = null;
                if (!"null".equalsIgnoreCase(fields[4])) {
                    originalLanguage = languageRepository.findById(Integer.parseInt(fields[4])).orElse(null);
                }

                Film film = Film.builder()
                        .title(fields[0])
                        .description(fields[1])
                        .releaseYear(Integer.parseInt(fields[2]))
                        .language(languageRepository.findById(Integer.parseInt(fields[3])).orElse(null))
                        .originalLanguage(originalLanguage)
                        .rentalDuration(Integer.parseInt(fields[5]))
                        .rentalRate(Double.parseDouble(fields[6]))
                        .length(Integer.parseInt(fields[7]))
                        .replacementCost(Double.parseDouble(fields[8]))
                        .rating(Rating.fromString(fields[9].replaceAll("\"", "").toUpperCase()))
                        .build();

                filmRepository.save(film);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInitialActorData() {
        try(BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/actor_data_cleaned.csv"))) {
            String line = reader.readLine();

            while((line = reader.readLine()) != null) {
                String[] fields = parseCsvLine(line);
                String firstName = fields[0];
                String lastName = fields[1];

                Actor actor = Actor.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .build();

                actorRepository.save(actor);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
