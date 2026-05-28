package com.abhiraj.PopcornPing.controllers;

import com.abhiraj.PopcornPing.domain.dtos.requests.LoginRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.requests.RegisterRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.requests.VerifyUserDto;
import com.abhiraj.PopcornPing.domain.dtos.response.JwtResponseDto;
import com.abhiraj.PopcornPing.domain.dtos.response.UserResponseDto;
import com.abhiraj.PopcornPing.services.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(authService.authenticate(loginRequestDto));
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto){
        try{
            authService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam @Email String email){
        try{
            authService.resendVerificationCode(email);
            return ResponseEntity.ok("Resent verification code");
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
