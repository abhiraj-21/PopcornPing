package com.abhiraj.PopcornPing.services;

import com.abhiraj.PopcornPing.domain.dtos.requests.LoginRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.requests.RegisterRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.requests.VerifyUserDto;
import com.abhiraj.PopcornPing.domain.dtos.response.JwtResponseDto;
import com.abhiraj.PopcornPing.domain.dtos.response.UserResponseDto;
import jakarta.validation.Valid;

public interface AuthService {
    UserResponseDto addNewUser(@Valid RegisterRequestDto registerRequestDto);

    JwtResponseDto authenticate(LoginRequestDto loginRequestDto);

    void verifyUser(VerifyUserDto user);

    void resendVerificationCode(String email);
}
