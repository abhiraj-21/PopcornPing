package com.abhiraj.PopcornPing.tmdbResponses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TmdbMovieDetail {
    private Long id;
    private String title;
    private String overview;
    @JsonProperty("poster_path")  private String posterPath;
    @JsonProperty("release_date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @JsonProperty("vote_average") private Double voteAverage;
    private Integer runtime;
    private String status;
    private List<Genre> genres;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Genre {
        private Integer id;
        private String name;
    }
}