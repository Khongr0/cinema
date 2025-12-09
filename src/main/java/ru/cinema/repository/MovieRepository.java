package ru.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cinema.model.Movie;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByGenreContainingIgnoreCase(String genre);
    List<Movie> findByDirectorContainingIgnoreCase(String director);
    
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(m.genre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(m.director) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Movie> searchMovies(@Param("keyword") String keyword);
    
    List<Movie> findByReleaseYear(Integer year);
    List<Movie> findAllByOrderByTitleAsc();
    List<Movie> findAllByOrderByReleaseYearDesc();
}

