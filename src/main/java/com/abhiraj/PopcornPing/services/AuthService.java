package com.abhiraj.PopcornPing.services;

import com.abhiraj.PopcornPing.domain.dtos.requests.LoginRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.requests.RegisterRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.response.JwtResponseDto;
import com.abhiraj.PopcornPing.domain.dtos.response.UserResponseDto;
import jakarta.validation.Valid;

public interface AuthService {
    UserResponseDto addNewUser(@Valid RegisterRequestDto registerRequestDto);

    JwtResponseDto verify(LoginRequestDto loginRequestDto);
}
