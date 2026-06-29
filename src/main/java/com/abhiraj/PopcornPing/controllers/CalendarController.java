package com.abhiraj.PopcornPing.controllers;

import com.abhiraj.PopcornPing.services.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/auth")
    public ResponseEntity<String> getAuthUrl() {
        return ResponseEntity.ok(calendarService.generateAuthUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam String code,
            Principal principal) {
        calendarService.handleOAuthCallback(code, principal.getName());
        return ResponseEntity.ok("Google Calendar connected successfully");
    }
}