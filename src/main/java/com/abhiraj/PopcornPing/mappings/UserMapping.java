package com.abhiraj.PopcornPing.mappings;

import com.abhiraj.PopcornPing.domain.dtos.requests.RegisterRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.response.UserResponseDto;
import com.abhiraj.PopcornPing.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapping {

    public User registerRequestToUser(RegisterRequestDto registerRequestDto){
        return User.builder()
                .email(registerRequestDto.getEmail())
                .password(registerRequestDto.getPassword())
                .build();
    }

    public UserResponseDto userToResponse(User user, Long movieCount){
        return UserResponseDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .movieCount(movieCount)
                .build();
    }

}
