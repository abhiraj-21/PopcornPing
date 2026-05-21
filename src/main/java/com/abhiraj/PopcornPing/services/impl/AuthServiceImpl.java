package com.abhiraj.PopcornPing.services.impl;

import com.abhiraj.PopcornPing.domain.dtos.requests.LoginRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.requests.RegisterRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.response.JwtResponseDto;
import com.abhiraj.PopcornPing.domain.dtos.response.UserResponseDto;
import com.abhiraj.PopcornPing.domain.entities.User;
import com.abhiraj.PopcornPing.jwt.JwtService;
import com.abhiraj.PopcornPing.mappings.UserMapping;
import com.abhiraj.PopcornPing.repositories.UserMovieTrackerRepository;
import com.abhiraj.PopcornPing.repositories.UserRepository;
import com.abhiraj.PopcornPing.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMovieTrackerRepository userMovieTrackerRepository;
    private final UserMapping userMapping;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public UserResponseDto addNewUser(RegisterRequestDto registerRequestDto) {
        User user = userMapping.registerRequestToUser(registerRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        Long movieCount = userMovieTrackerRepository.countByUserId(savedUser.getId());
        return userMapping.userToResponse(savedUser, movieCount);
    }

    @Override
    public JwtResponseDto verify(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() ->
                    new UsernameNotFoundException("No user with email "+loginRequestDto.getEmail())
                );
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(), loginRequestDto.getPassword()
        ));
        if(!authentication.isAuthenticated()){
            throw new BadCredentialsException("Inavlid email or password");
        }

        return jwtService.generateToken(user);
    }
}
