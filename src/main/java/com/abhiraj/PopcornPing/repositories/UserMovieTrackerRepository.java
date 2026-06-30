package com.abhiraj.PopcornPing.repositories;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.domain.entities.User;
import com.abhiraj.PopcornPing.domain.entities.UserMovieTracker;
import com.abhiraj.PopcornPing.domain.enums.WatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMovieTrackerRepository extends JpaRepository<UserMovieTracker, Long> {
    Long countByUserId(Long id);

    boolean existsByUserAndMovie(User user, Movie movie);

    List<UserMovieTracker> findByUserAndWatchStatus(User user, WatchStatus watchStatus);

    Optional<UserMovieTracker> findByUserAndMovieTmdbId(User user, Long movieId);

    List<UserMovieTracker> findByMovieTmdbIdAndWatchStatus(Long movieId, WatchStatus watchStatus);
}
