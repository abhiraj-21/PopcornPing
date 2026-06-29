package com.abhiraj.PopcornPing.services.impl;

import com.abhiraj.PopcornPing.config.GoogleOAuthConfig;
import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.domain.entities.User;
import com.abhiraj.PopcornPing.repositories.UserRepository;
import com.abhiraj.PopcornPing.services.CalendarService;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final GoogleOAuthConfig googleOAuthConfig;
    private final UserRepository userRepository;

    @Override
    public String generateAuthUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + googleOAuthConfig.getClientId()
                + "&redirect_uri=" + googleOAuthConfig.getRedirectUri()
                + "&response_type=code"
                + "&scope=" + googleOAuthConfig.getScope()
                + "&access_type=offline"
                + "&prompt=consent";
    }

    @Override
    public void handleOAuthCallback(String code, String email) {
        try {
            NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    transport,
                    jsonFactory,
                    googleOAuthConfig.getClientId(),
                    googleOAuthConfig.getClientSecret(),
                    code,
                    googleOAuthConfig.getRedirectUri()
            ).execute();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            LocalDateTime expiry = LocalDateTime.now()
                    .plusSeconds(tokenResponse.getExpiresInSeconds());

            user.setGoogleAccessToken(tokenResponse.getAccessToken());
            user.setGoogleRefreshToken(tokenResponse.getRefreshToken());
            user.setGoogleTokenExpiry(expiry);
            user.setIsCalendarConnected(true);

            userRepository.save(user);
            log.info("Google Calendar connected for user: {}", email);

        } catch (IOException | GeneralSecurityException e) {
            log.error("Failed to exchange OAuth code: {}", e.getMessage());
            throw new RuntimeException("Failed to connect Google Calendar", e);
        }
    }

    private Calendar buildCalendarClient(User user) throws IOException {
        if (user.getGoogleTokenExpiry().isBefore(LocalDateTime.now().plusMinutes(5))) {
            refreshAccessToken(user);
        }

        AccessToken accessToken = new AccessToken(
                user.getGoogleAccessToken(),
                Date.from(user.getGoogleTokenExpiry()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant())
        );

        GoogleCredentials credentials = UserCredentials.newBuilder()
                .setClientId(googleOAuthConfig.getClientId())
                .setClientSecret(googleOAuthConfig.getClientSecret())
                .setRefreshToken(user.getGoogleRefreshToken())
                .setAccessToken(accessToken)
                .build();

        try {
            return new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName("PopcornPing")
                    .build();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to build calendar client", e);
        }
    }

    private void refreshAccessToken(User user) throws IOException {
        UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(googleOAuthConfig.getClientId())
                .setClientSecret(googleOAuthConfig.getClientSecret())
                .setRefreshToken(user.getGoogleRefreshToken())
                .build();

        credentials.refresh();

        user.setGoogleAccessToken(credentials.getAccessToken().getTokenValue());
        user.setGoogleTokenExpiry(LocalDateTime.now().plusSeconds(3600));
        userRepository.save(user);
    }


    @Override
    public String createReleaseEvent(User user, Movie movie) {
        if (movie.getReleaseDate().isBefore(LocalDate.now())) {
            log.info("Skipping calendar event — {} already released", movie.getTitle());
            return null;
        }

        try {
            Calendar calendar = buildCalendarClient(user);
            Event event = buildEvent(movie);
            Event created = calendar.events().insert("primary", event).execute();
            log.info("Calendar event created for movie: {}", movie.getTitle());
            return created.getId();
        } catch (IOException e) {
            log.error("Failed to create calendar event: {}", e.getMessage());
            throw new RuntimeException("Failed to create calendar event", e);
        }
    }

    @Override
    public void deleteEvent(User user, String calendarEventId) {
        if (calendarEventId == null) return;
        try {
            Calendar calendar = buildCalendarClient(user);
            calendar.events().delete("primary", calendarEventId).execute();
            log.info("Calendar event deleted: {}", calendarEventId);
        } catch (IOException e) {
            log.warn("Failed to delete calendar event {}: {}", calendarEventId, e.getMessage());
        }
    }

    @Override
    public String updateEvent(User user, String existingEventId, Movie movie) {
        deleteEvent(user, existingEventId);
        return createReleaseEvent(user, movie);
    }

    private Event buildEvent(Movie movie) {
        DateTime releaseDay = new DateTime(movie.getReleaseDate().toString());
        EventDateTime eventDateTime = new EventDateTime()
                .setDate(releaseDay)
                .setTimeZone("UTC");

        return new Event()
                .setSummary("🎬 " + movie.getTitle() + " releases today!")
                .setDescription(
                        "PopcornPing reminder!\n\n"
                                + movie.getTitle() + "\n"
                                + "Rating: " + movie.getVoteAverage() + "\n\n"
                                + movie.getOverview()
                )
                .setStart(eventDateTime)
                .setEnd(eventDateTime);
    }
}