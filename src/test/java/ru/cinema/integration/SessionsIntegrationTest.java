package ru.cinema.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.cinema.model.Hall;
import ru.cinema.model.Movie;
import ru.cinema.model.Session;
import ru.cinema.repository.HallRepository;
import ru.cinema.repository.MovieRepository;
import ru.cinema.repository.SessionRepository;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SessionsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        movieRepository.deleteAll();
        hallRepository.deleteAll();

        Movie m = new Movie();
        m.setTitle("Integration Test Movie");
        m.setDescription("desc");
        m.setGenre("drama");
        m.setDuration(120);
        m.setReleaseYear(2020);
        m.setDirector("Dir");
        movieRepository.save(m);

        Hall h = new Hall();
        h.setName("Test Hall");
        h.setCapacity(50);
        h.setHallType("Normal");
        hallRepository.save(h);

        Session s = new Session();
        s.setMovie(m);
        s.setHall(h);
        s.setStartTime(LocalDateTime.now().plusDays(1));
        s.setPrice(9.99);
        sessionRepository.save(s);
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "admin", roles = {"ADMIN"})
    void sessionsPage_returns200_andContainsMovieTitle() throws Exception {
        mockMvc.perform(get("/sessions").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Integration Test Movie")));
    }
}
