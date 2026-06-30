package com.abhiraj.PopcornPing.controllers;

import com.abhiraj.PopcornPing.Schedulers.MovieScheduler;
import com.abhiraj.PopcornPing.domain.dtos.response.MovieResponseDto;
import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final MovieScheduler movieScheduler;
    private final MovieRepository movieRepository;

    @PostMapping("/trigger-sync")
    public ResponseEntity<String> triggerSync() {
        movieScheduler.movieDataConsistencyCheck();
        return ResponseEntity.ok("Sync triggered");
    }

    @GetMapping("/movies")
    public ResponseEntity<Page<MovieResponseDto>> listMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("releaseDate").descending());
        Page<Movie> movies = movieRepository.findAll(pageable);

        Page<MovieResponseDto> response = movies.map(movie -> MovieResponseDto.builder()
                .id(movie.getTmdbId())
                .title(movie.getTitle())
                .overview(movie.getOverview())
                .releaseDate(movie.getReleaseDate())
                .rating(movie.getVoteAverage())
                .build());

        return ResponseEntity.ok(response);
    }
}