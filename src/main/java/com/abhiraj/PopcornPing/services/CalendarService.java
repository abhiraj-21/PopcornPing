package com.abhiraj.PopcornPing.services;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.domain.entities.User;

public interface CalendarService {
    String createReleaseEvent(User user, Movie movie);
}
