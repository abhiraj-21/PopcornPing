package com.abhiraj.PopcornPing.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "movie")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Movie {

    @Id
    private Long tmdbId;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String overview;

    @Column(nullable = false)
    private Double voteAverage;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false)
    private String posterUrl;

    @Column(nullable = false)
    private LocalDateTime lastSyncedAt;

    @OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<UserMovieTracker> lists = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (lastSyncedAt == null) {
            lastSyncedAt = LocalDateTime.now();
        }
    }

}
