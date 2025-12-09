package ru.cinema.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.cinema.model.Hall;
import ru.cinema.model.Movie;
import ru.cinema.model.Session;
import ru.cinema.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionServiceTest {

    @Test
    void saveSession_setsAvailableSeats_whenMissing() {
        SessionRepository mockRepo = Mockito.mock(SessionRepository.class);
        SessionService service = new SessionService(mockRepo);

        Movie m = new Movie(); m.setId(1L); m.setTitle("Test"); m.setDuration(90);
        Hall h = new Hall(); h.setId(1L); h.setName("Main"); h.setCapacity(100);

        Session s = new Session();
        s.setMovie(m);
        s.setHall(h);
        s.setStartTime(LocalDateTime.now().plusDays(1));
        s.setPrice(10.0);

        Mockito.when(mockRepo.save(Mockito.any(Session.class))).thenAnswer(i -> i.getArgument(0));

        Session saved = service.saveSession(s);

        assertThat(saved.getAvailableSeats()).isEqualTo(100);
        Mockito.verify(mockRepo).save(saved);
    }

    @Test
    void searchSessions_delegatesToRepository() {
        SessionRepository mockRepo = Mockito.mock(SessionRepository.class);
        SessionService service = new SessionService(mockRepo);

        Mockito.when(mockRepo.searchSessions("abc")).thenReturn(List.of());

        List<Session> res = service.searchSessions("abc");

        assertThat(res).isEmpty();
        Mockito.verify(mockRepo).searchSessions("abc");
    }
}
