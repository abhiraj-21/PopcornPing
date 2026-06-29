package com.abhiraj.PopcornPing.controllers;

import com.abhiraj.PopcornPing.domain.dtos.response.UserMovieTrackerResponseDto;
import com.abhiraj.PopcornPing.domain.enums.WatchStatus;
import com.abhiraj.PopcornPing.services.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @PostMapping("/{tmdbId}")
    public ResponseEntity<?> addMovie(
            Principal principal,
            @PathVariable Long tmdbId,
            @RequestParam(required = false, defaultValue = "WANT_TO_WATCH")WatchStatus watchStatus){
        try{
            UserMovieTrackerResponseDto response = watchlistService.addMovieToWatchlist(principal.getName(), tmdbId, watchStatus);
            return ResponseEntity.ok(response);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<UserMovieTrackerResponseDto>> moviesWithStatus(
            Principal principal,
            @RequestParam(defaultValue = "WATCHED") WatchStatus status){
        return ResponseEntity.ok(watchlistService.retrieveMovieWithStatus(principal.getName(), status));
    }

    @PatchMapping("/{tmdbId}")
    public ResponseEntity<?> changeStatus(
            Principal principal,
            @PathVariable Long tmdbId,
            @RequestParam WatchStatus status){
        try{
            return ResponseEntity.ok(watchlistService.updateMovieStatus(principal.getName(), status, tmdbId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{tmdbId}")
    public ResponseEntity<?> deleteMovie(Principal principal, @PathVariable Long tmdbId){
        return ResponseEntity.ok(watchlistService.deleteMovie(principal.getName(), tmdbId));
    }

}
