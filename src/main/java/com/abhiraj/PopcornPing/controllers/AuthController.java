package com.abhiraj.PopcornPing.controllers;

import com.abhiraj.PopcornPing.domain.dtos.requests.LoginRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.requests.RegisterRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.response.JwtResponseDto;
import com.abhiraj.PopcornPing.domain.dtos.response.UserResponseDto;
import com.abhiraj.PopcornPing.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/register")
    public ResponseEntity<UserResponseDto> registerNewUser(@Valid @RequestBody RegisterRequestDto registerRequestDto){
        return ResponseEntity.ok(authService.addNewUser(registerRequestDto));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authService.verify(loginRequestDto));
    }

}
