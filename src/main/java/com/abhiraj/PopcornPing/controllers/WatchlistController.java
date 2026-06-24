package com.abhiraj.PopcornPing.controllers;

import com.abhiraj.PopcornPing.domain.dtos.response.UserMovieTrackerResponseDto;
import com.abhiraj.PopcornPing.domain.enums.WatchStatus;
import com.abhiraj.PopcornPing.services.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

}
