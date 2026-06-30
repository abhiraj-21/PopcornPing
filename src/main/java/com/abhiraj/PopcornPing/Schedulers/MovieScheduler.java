package com.abhiraj.PopcornPing.Schedulers;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.repositories.MovieRepository;
import com.abhiraj.PopcornPing.services.MovieSyncService;
import com.abhiraj.PopcornPing.services.TmdbClientService;
import com.abhiraj.PopcornPing.tmdbResponses.TmdbMovieDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MovieScheduler {

    private final MovieRepository movieRepository;
    private final TmdbClientService tmdbClientService;
    private final MovieSyncService movieSyncService;

    @Scheduled(cron = "0 30 0 * * ?")
    public void movieDataConsistencyCheck() {
        List<Movie> movies = movieRepository.findAll();

        for (Movie movie : movies) {
            try {
                TmdbMovieDetail tmdbMovie = tmdbClientService.getMovieById(movie.getTmdbId());

                if (!tmdbMovie.getReleaseDate().equals(movie.getReleaseDate())) {
                    movieSyncService.processMovieDateChange(movie, tmdbMovie);
                }

                Thread.sleep(150);

            } catch (Exception e) {
                log.error("Failed to sync movie {}: {}", movie.getTmdbId(), e.getMessage());
            }
        }
    }
}