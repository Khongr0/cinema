package ru.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cinema.model.Movie;
import ru.cinema.model.Session;
import ru.cinema.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SessionService {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    public Session saveSession(Session session) {
        if (session.getHall() != null && session.getAvailableSeats() == null) {
            session.setAvailableSeats(session.getHall().getCapacity());
        }
        return sessionRepository.save(session);
    }

    public Session updateSession(Session session) {
        if (!sessionRepository.existsById(session.getId())) {
            throw new IllegalArgumentException("Сеанс не найден");
        }
        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new IllegalArgumentException("Сеанс не найден");
        }
        sessionRepository.deleteById(id);
    }

    public List<Session> searchSessions(String keyword) {
        return sessionRepository.searchSessions(keyword);
    }

    public List<Session> getSessionsByMovie(Movie movie) {
        return sessionRepository.findByMovie(movie);
    }

    public List<Session> getUpcomingSessions() {
        return sessionRepository.findByStartTimeAfter(LocalDateTime.now());
    }

    public List<Session> getSessionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return sessionRepository.findByStartTimeBetween(start, end);
    }

    public List<Session> sortSessionsByTime() {
        return sessionRepository.findAllByOrderByStartTimeAsc();
    }

    public List<Session> sortSessionsByPrice() {
        return sessionRepository.findAllByOrderByPriceAsc();
    }
}

