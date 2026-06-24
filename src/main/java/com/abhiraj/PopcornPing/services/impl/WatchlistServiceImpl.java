package com.abhiraj.PopcornPing.services.impl;

import com.abhiraj.PopcornPing.domain.dtos.response.UserMovieTrackerResponseDto;
import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.domain.entities.User;
import com.abhiraj.PopcornPing.domain.entities.UserMovieTracker;
import com.abhiraj.PopcornPing.domain.enums.NotificationStatus;
import com.abhiraj.PopcornPing.domain.enums.WatchStatus;
import com.abhiraj.PopcornPing.mappings.MovieMapping;
import com.abhiraj.PopcornPing.mappings.UserMovieTrackerMapping;
import com.abhiraj.PopcornPing.repositories.MovieRepository;
import com.abhiraj.PopcornPing.repositories.UserMovieTrackerRepository;
import com.abhiraj.PopcornPing.repositories.UserRepository;
import com.abhiraj.PopcornPing.services.CalendarService;
import com.abhiraj.PopcornPing.services.TmdbClientService;
import com.abhiraj.PopcornPing.services.WatchlistService;
import com.abhiraj.PopcornPing.tmdbResponses.TmdbMovieDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WatchlistServiceImpl implements WatchlistService {

    private final UserRepository userRepository;
    private final TmdbClientService tmdbClientService;
    private final UserMovieTrackerRepository userMovieTrackerRepository;
    private final MovieMapping movieMapping;
    private final CalendarService calendarService;
    private final UserMovieTrackerMapping userMovieTrackerMapping;
    private final MovieRepository movieRepository;

    @Override
    @Transactional
    public UserMovieTrackerResponseDto addMovieToWatchlist(String email, Long tmdbId, WatchStatus watchStatus) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
            new UsernameNotFoundException("No user with email "+email)
        );

        Movie movie = movieRepository.findById(tmdbId)
                .orElseGet(() -> {
                    TmdbMovieDetail tmdbMovie = tmdbClientService.getMovieById(tmdbId);
                    return movieRepository.save(
                            movieMapping.tmdbMovieToMovie(tmdbMovie)
                    );
                });

        movie.setLastSyncedAt(LocalDateTime.now());

        if (userMovieTrackerRepository.existsByUserAndMovie(user, movie)) {
            throw new IllegalStateException("Movie already in watchlist");
        }

        String releaseEvent = calendarService.createReleaseEvent(user, movie);

        UserMovieTracker userMovieTracker = UserMovieTracker.builder()
                .movie(movie)
                .calendarEventId(releaseEvent)
                .user(user)
                .watchStatus(watchStatus)
                .notificationStatus(NotificationStatus.PENDING)
                .build();

        UserMovieTracker saved = userMovieTrackerRepository.save(userMovieTracker);

        return userMovieTrackerMapping.domainToResponse(saved);
    }
}
