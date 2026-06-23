package com.abhiraj.PopcornPing.services.impl;

import com.abhiraj.PopcornPing.services.TmdbClientService;
import com.abhiraj.PopcornPing.tmdbResponses.TmdbMovieDetail;
import com.abhiraj.PopcornPing.tmdbResponses.TmdbPagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class TmdbClientServiceImpl implements TmdbClientService {

    @Value("${tmdb.api.token}")
    private String apiToken;

    @Value("${tmdb.api.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    private HttpEntity<Void> authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiToken);
        return new HttpEntity<>(headers);
    }

    private <T> T get(String path, Class<T> responseType) {
        ResponseEntity<T> response = restTemplate.exchange(
                baseUrl + path, HttpMethod.GET, authHeaders(), responseType
        );
        return response.getBody();
    }

    @Override
    public TmdbPagedResponse getPopularMovies() {
        return get("/movie/popular?language=en-US&page=1", TmdbPagedResponse.class);
    }

    @Override
    public TmdbPagedResponse searchMovies(String query) {
        String encoded = UriUtils.encode(query, StandardCharsets.UTF_8);
        return get("/search/movie?query=" + encoded + "&language=en-US&page=1", TmdbPagedResponse.class);
    }

    @Override
    public TmdbMovieDetail getMovieById(Long tmdbId) {
        return get("/movie/" + tmdbId + "?language=en-US", TmdbMovieDetail.class);
    }

    @Override
    public TmdbPagedResponse getUpcomingMovies() {
        return get("/movie/upcoming?language=en-US&page=1", TmdbPagedResponse.class);
    }
}