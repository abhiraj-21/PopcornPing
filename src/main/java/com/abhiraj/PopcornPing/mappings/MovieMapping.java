package com.abhiraj.PopcornPing.mappings;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.tmdbResponses.TmdbMovieDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieMapping {

    public Movie tmdbMovieToMovie(TmdbMovieDetail movieDetail){
        return Movie.builder()
                .tmdbId(movieDetail.getId())
                .title(movieDetail.getTitle())
                .overview(movieDetail.getOverview())
                .voteAverage(movieDetail.getVoteAverage())
                .releaseDate(movieDetail.getReleaseDate())
                .posterUrl("https://image.tmdb.org/t/p/w500" + movieDetail.getPosterPath())
                .build();
    }

}
