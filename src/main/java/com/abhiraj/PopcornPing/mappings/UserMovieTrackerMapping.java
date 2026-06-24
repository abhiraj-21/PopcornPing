package com.abhiraj.PopcornPing.mappings;

import com.abhiraj.PopcornPing.domain.dtos.response.UserMovieTrackerResponseDto;
import com.abhiraj.PopcornPing.domain.entities.UserMovieTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMovieTrackerMapping {

    public UserMovieTrackerResponseDto domainToResponse(UserMovieTracker userMovieTracker){
        return UserMovieTrackerResponseDto.builder()
                .id(userMovieTracker.getId())
                .movieName(userMovieTracker.getMovie().getTitle())
                .userName(userMovieTracker.getUser().getEmail())
                .watchStatus(String.valueOf(userMovieTracker.getWatchStatus()))
                .build();
    }

}
