package com.abhiraj.PopcornPing.services.impl;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.domain.entities.User;
import com.abhiraj.PopcornPing.services.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CalendarServiceImpl implements CalendarService {

    @Override
    public String createReleaseEvent(User user, Movie movie) {
        log.info("Mock calendar event for the movie {} and the user {}", movie.getTmdbId(), user.getEmail());
        return "mock_google_event_id_"+movie.getTmdbId();
    }
}
