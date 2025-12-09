package ru.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cinema.model.Movie;
import ru.cinema.model.Session;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByMovie(Movie movie);
    List<Session> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Session> findByStartTimeAfter(LocalDateTime start);
    List<Session> findAllByOrderByStartTimeAsc();
    List<Session> findAllByOrderByPriceAsc();
    
    @Query("SELECT s FROM Session s WHERE LOWER(s.movie.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.hall.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Session> searchSessions(@Param("keyword") String keyword);
}

