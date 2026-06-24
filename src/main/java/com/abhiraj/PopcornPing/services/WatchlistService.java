package com.abhiraj.PopcornPing.services;

import com.abhiraj.PopcornPing.domain.dtos.response.UserMovieTrackerResponseDto;
import com.abhiraj.PopcornPing.domain.enums.WatchStatus;

public interface WatchlistService {
    UserMovieTrackerResponseDto addMovieToWatchlist(String email, Long tmdbId, WatchStatus watchStatus);
}
