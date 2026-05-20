package com.abhiraj.PopcornPing.domain.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MovieResponseDto {

    private Long id;
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private Double rating;
    private String watchStatus;

}
