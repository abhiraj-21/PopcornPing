package com.abhiraj.PopcornPing.domain.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserMovieTrackerResponseDto {

    private Long id;
    private String userName;
    private String movieName;
    private String watchStatus;
    private Boolean calendarEventCreated;
    private String calendarAuthUrl;

}
