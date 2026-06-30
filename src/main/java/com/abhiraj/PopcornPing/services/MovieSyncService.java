package com.abhiraj.PopcornPing.services;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.tmdbResponses.TmdbMovieDetail;

public interface MovieSyncService {
    void processMovieDateChange(Movie movie, TmdbMovieDetail tmdbMovie);

    void processNotifyUsers(Movie movie);
}