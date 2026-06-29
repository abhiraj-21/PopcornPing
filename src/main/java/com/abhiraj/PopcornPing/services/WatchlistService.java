package com.abhiraj.PopcornPing.services;

import com.abhiraj.PopcornPing.domain.dtos.response.UserMovieTrackerResponseDto;
import com.abhiraj.PopcornPing.domain.enums.WatchStatus;

import java.util.List;

public interface WatchlistService {
    UserMovieTrackerResponseDto addMovieToWatchlist(String email, Long tmdbId, WatchStatus watchStatus);

    List<UserMovieTrackerResponseDto> retrieveMovieWithStatus(String email, WatchStatus watchStatus);

    UserMovieTrackerResponseDto updateMovieStatus(String email, WatchStatus status, Long tmdbId);

    Void deleteMovie(String email, Long tmdbId);
}
