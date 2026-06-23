package com.abhiraj.PopcornPing.controllers;

import com.abhiraj.PopcornPing.services.TmdbClientService;
import com.abhiraj.PopcornPing.tmdbResponses.TmdbMovieDetail;
import com.abhiraj.PopcornPing.tmdbResponses.TmdbPagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final TmdbClientService tmdbClientService;

    @GetMapping("/popular")
    public ResponseEntity<TmdbPagedResponse> popular() {
        return ResponseEntity.ok(tmdbClientService.getPopularMovies());
    }

    @GetMapping("/search")
    public ResponseEntity<TmdbPagedResponse> search(@RequestParam String query) {
        return ResponseEntity.ok(tmdbClientService.searchMovies(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TmdbMovieDetail> detail(@PathVariable Long id) {
        return ResponseEntity.ok(tmdbClientService.getMovieById(id));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<TmdbPagedResponse> upcoming() {
        return ResponseEntity.ok(tmdbClientService.getUpcomingMovies());
    }
}
