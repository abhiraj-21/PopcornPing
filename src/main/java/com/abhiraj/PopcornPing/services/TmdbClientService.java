package com.abhiraj.PopcornPing.services;

import com.abhiraj.PopcornPing.tmdbResponses.TmdbMovieDetail;
import com.abhiraj.PopcornPing.tmdbResponses.TmdbPagedResponse;

public interface TmdbClientService {
    TmdbPagedResponse getPopularMovies();
    TmdbPagedResponse searchMovies(String query);
    TmdbMovieDetail getMovieById(Long tmdbId);
    TmdbPagedResponse getUpcomingMovies();
}
