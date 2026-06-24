package com.abhiraj.PopcornPing.repositories;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import com.abhiraj.PopcornPing.domain.entities.User;
import com.abhiraj.PopcornPing.domain.entities.UserMovieTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieTrackerRepository extends JpaRepository<UserMovieTracker, Long> {
    Long countByUserId(Long id);

    boolean existsByUserAndMovie(User user, Movie movie);
}
