package com.abhiraj.PopcornPing.controllers;

import com.abhiraj.PopcornPing.domain.dtos.response.UserResponseDto;
import com.abhiraj.PopcornPing.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getUser(){
        return ResponseEntity.ok(userService.getUser());
    }

}
