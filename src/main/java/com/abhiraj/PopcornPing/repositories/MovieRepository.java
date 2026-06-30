package com.abhiraj.PopcornPing.repositories;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByReleaseDate(LocalDate localDate);
}
