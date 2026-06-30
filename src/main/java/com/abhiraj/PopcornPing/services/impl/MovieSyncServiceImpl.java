package com.abhiraj.PopcornPing.services.impl;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.domain.entities.UserMovieTracker;
import com.abhiraj.PopcornPing.domain.enums.NotificationStatus;
import com.abhiraj.PopcornPing.domain.enums.WatchStatus;
import com.abhiraj.PopcornPing.repositories.MovieRepository;
import com.abhiraj.PopcornPing.repositories.UserMovieTrackerRepository;
import com.abhiraj.PopcornPing.services.CalendarService;
import com.abhiraj.PopcornPing.services.EmailService;
import com.abhiraj.PopcornPing.services.MovieSyncService;
import com.abhiraj.PopcornPing.tmdbResponses.TmdbMovieDetail;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieSyncServiceImpl implements MovieSyncService {

    private final MovieRepository movieRepository;
    private final UserMovieTrackerRepository userMovieTrackerRepository;
    private final CalendarService calendarService;
    private final EmailService emailService;

    @Override
    @Transactional
    public void processMovieDateChange(Movie movie, TmdbMovieDetail tmdbMovie) {
        movie.setReleaseDate(tmdbMovie.getReleaseDate());
        movie.setLastSyncedAt(LocalDateTime.now());
        movieRepository.save(movie);

        List<UserMovieTracker> trackedMovies = userMovieTrackerRepository
                .findByMovieTmdbIdAndWatchStatus(movie.getTmdbId(), WatchStatus.WANT_TO_WATCH);

        for (UserMovieTracker trackedMovie : trackedMovies) {
            try {
                notifyTracker(trackedMovie, movie);
            } catch (Exception e) {
                log.error("Failed to notify user {} for movie {}: {}",
                        trackedMovie.getUser().getEmail(), movie.getTitle(), e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void processNotifyUsers(Movie movie) {
        List<UserMovieTracker> trackedMovies = userMovieTrackerRepository
                .findByMovieTmdbIdAndWatchStatusAndNotificationStatus(movie.getTmdbId(), WatchStatus.WANT_TO_WATCH, NotificationStatus.PENDING);

        for(UserMovieTracker trackedMovie : trackedMovies){
            try{
                notifyUser(trackedMovie, movie);
            }catch(Exception e){
                log.error("Failed to notify user {} for movie {}: {}",
                        trackedMovie.getUser().getEmail(), movie.getTitle(), e.getMessage());
            }
        }
    }

    private void notifyUser(UserMovieTracker trackedMovie, Movie movie) throws MessagingException {
        String subject = "Movie '"+movie.getTitle()+"' releases tomorrow";
        String htmlMessage = "<!DOCTYPE html>"
                + "<html><body style='font-family:Arial,sans-serif;background:#f4f4f4;padding:20px'>"
                + "<div style='max-width:500px;margin:auto;background:#fff;padding:30px;border-radius:10px;text-align:center'>"
                + "<h1 style='color:#ff6b6b'> PopcornPing</h1>"
                + "<p>Your watchlist reminder is here! </p>"
                + "<p>One of the movies in your watchlist is releasing <b>tomorrow</b>.</p>"
                + "<h2 style='color:#ff6b6b;letter-spacing:1px'>" + movie.getTitle() + "</h2>"
                + "<p>Release Date:</p>"
                + "<h3 style='color:#333;'>" + movie.getReleaseDate() + "</h3>"
                + "<p style='margin-top:10px'>Get your popcorn ready and don't miss the premiere! 🍿</p>"
                + "<p style='color:gray;font-size:12px;margin-top:20px'>You're receiving this reminder because this movie is in your watchlist.</p>"
                + "</div>"
                + "</body></html>";

        emailService.sendEmail(trackedMovie.getUser().getEmail(), subject, htmlMessage);
        trackedMovie.setNotificationStatus(NotificationStatus.NOTIFIED);
        userMovieTrackerRepository.save(trackedMovie);
    }

    private void notifyTracker(UserMovieTracker trackedMovie, Movie movie) throws MessagingException {
        if (trackedMovie.getCalendarEventId() != null && !trackedMovie.getCalendarEventId().isEmpty()) {
            calendarService.updateEvent(trackedMovie.getUser(), trackedMovie.getCalendarEventId(), movie);
        }

        String subject = "Movie '" + movie.getTitle() + "' release date changed";
        String htmlMessage = "<!DOCTYPE html>"
                + "<html><body style='font-family:Arial,sans-serif;background:#f4f4f4;padding:20px'>"
                + "<div style='max-width:500px;margin:auto;background:#fff;padding:30px;border-radius:10px;text-align:center'>"
                + "<h1 style='color:#ff6b6b'>PopcornPing</h1>"
                + "<p>Good news / update from your watchlist </p>"
                + "<p>The release date for one of your saved movies has been changed.</p>"
                + "<h2 style='color:#ff6b6b;letter-spacing:1px'>" + movie.getTitle() + "</h2>"
                + "<p>New release date:</p>"
                + "<h3 style='color:#333;'>" + movie.getReleaseDate() + "</h3>"
                + "<p style='margin-top:10px'>Your calendar event has been <b>automatically updated</b> to reflect this change.</p>"
                + "<p style='color:gray;font-size:12px;margin-top:20px'>If you didn't expect this update, you can review your watchlist settings.</p>"
                + "</div>"
                + "</body></html>";

        emailService.sendEmail(trackedMovie.getUser().getEmail(), subject, htmlMessage);

        trackedMovie.setNotificationStatus(NotificationStatus.PENDING);
        userMovieTrackerRepository.save(trackedMovie);
    }
}