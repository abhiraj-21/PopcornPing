package com.abhiraj.PopcornPing.repositories;

import com.abhiraj.PopcornPing.domain.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
