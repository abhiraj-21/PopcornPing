package com.abhiraj.PopcornPing.services;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.domain.entities.User;

public interface CalendarService {
    String generateAuthUrl();
    void handleOAuthCallback(String code, String email);
    String createReleaseEvent(User user, Movie movie);
    void deleteEvent(User user, String calendarEventId);
    String updateEvent(User user, String existingEventId, Movie movie);
}