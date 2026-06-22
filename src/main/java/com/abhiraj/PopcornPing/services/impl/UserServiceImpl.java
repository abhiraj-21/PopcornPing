package com.abhiraj.PopcornPing.services.impl;

import com.abhiraj.PopcornPing.domain.dtos.response.UserResponseDto;
import com.abhiraj.PopcornPing.domain.entities.User;
import com.abhiraj.PopcornPing.mappings.UserMapping;
import com.abhiraj.PopcornPing.repositories.MovieRepository;
import com.abhiraj.PopcornPing.repositories.UserMovieTrackerRepository;
import com.abhiraj.PopcornPing.repositories.UserRepository;
import com.abhiraj.PopcornPing.services.CurrentUserService;
import com.abhiraj.PopcornPing.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final UserMovieTrackerRepository userMovieTrackerRepository;
    private final UserMapping userMapping;

    @Override
    public UserResponseDto getUser() {
        User principal = currentUserService.getAuthenticatedUser();
        User freshUser = userRepository.findByEmail(principal.getEmail()).orElseThrow(() ->
                    new UsernameNotFoundException("No User with user email "+principal.getEmail())
                );
        long movieCount = userMovieTrackerRepository.countByUserId(freshUser.getId());
        return userMapping.userToResponse(freshUser, movieCount);
    }
}
