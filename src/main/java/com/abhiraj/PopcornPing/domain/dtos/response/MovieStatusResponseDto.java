package com.abhiraj.PopcornPing.domain.dtos.response;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.domain.enums.WatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MovieStatusResponseDto {

    private String userName;
    private String movieName;
    private WatchStatus status;

}
