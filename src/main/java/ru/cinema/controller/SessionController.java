package ru.cinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cinema.model.Session;
import ru.cinema.service.HallService;
import ru.cinema.service.MovieService;
import ru.cinema.service.SessionService;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/sessions")
public class SessionController {
    private final SessionService sessionService;
    private final MovieService movieService;
    private final HallService hallService;

    @Autowired
    public SessionController(SessionService sessionService, MovieService movieService, HallService hallService) {
        this.sessionService = sessionService;
        this.movieService = movieService;
        this.hallService = hallService;
    }

    @GetMapping
    public String listSessions(@RequestParam(required = false) String search,
                              @RequestParam(required = false) String sort,
                              Model model) {
        List<Session> sessions;
        
        if (search != null && !search.isEmpty()) {
            sessions = sessionService.searchSessions(search);
            model.addAttribute("search", search);
        } else if ("time".equals(sort)) {
            sessions = sessionService.sortSessionsByTime();
            model.addAttribute("sort", sort);
        } else if ("price".equals(sort)) {
            sessions = sessionService.sortSessionsByPrice();
            model.addAttribute("sort", sort);
        } else {
            sessions = sessionService.getAllSessions();
        }
        
        model.addAttribute("sessions", sessions);
        return "sessions/list";
    }

    @GetMapping("/new")
    public String showSessionForm(Model model) {
        model.addAttribute("session", new Session());
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("halls", hallService.getAllHalls());
        return "sessions/form";
    }

    @PostMapping
    public String saveSession(@ModelAttribute Session session,
                             @RequestParam(value = "movieId", required = false) Long movieId,
                             @RequestParam(value = "hallId", required = false) Long hallId,
                             @RequestParam(value = "startTime", required = false) String startTimeStr,
                             @RequestParam(value = "price", required = false) Double price,
                             RedirectAttributes redirectAttributes, Model model) {
        try {
            // Валидация входных данных
            if (movieId == null || movieId <= 0) {
                throw new IllegalArgumentException("Фильм не выбран");
            }
            if (hallId == null || hallId <= 0) {
                throw new IllegalArgumentException("Зал не выбран");
            }
            if (startTimeStr == null || startTimeStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Время начала не указано");
            }
            if (price == null || price < 0) {
                throw new IllegalArgumentException("Цена должна быть указана и не может быть отрицательной");
            }
            
            // Парсим дату из формата datetime-local (yyyy-MM-ddTHH:mm)
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startTime;
            try {
                String cleanedTime = startTimeStr.trim().replace(" ", "T");
                if (!cleanedTime.contains("T")) {
                    throw new IllegalArgumentException("Неверный формат даты");
                }
                startTime = LocalDateTime.parse(cleanedTime, fmt);
            } catch (Exception e) {
                throw new IllegalArgumentException("Неверный формат даты и времени. Используйте формат: ГГГГ-ММ-ДД ЧЧ:ММ");
            }
            
            // Получаем фильм и зал
            var movie = movieService.getMovieById(movieId)
                    .orElseThrow(() -> new IllegalArgumentException("Фильм не найден"));
            var hall = hallService.getHallById(hallId)
                    .orElseThrow(() -> new IllegalArgumentException("Зал не найден"));
            
            // Устанавливаем данные сеанса
            session.setMovie(movie);
            session.setHall(hall);
            session.setStartTime(startTime);
            session.setPrice(price);
            
            // Сохраняем сеанс
            Session savedSession = sessionService.saveSession(session);
            System.out.println("✅ Сеанс успешно сохранен: ID=" + savedSession.getId() + 
                             ", Фильм=" + savedSession.getMovie().getTitle() + 
                             ", Время=" + savedSession.getStartTime());
            
            redirectAttributes.addFlashAttribute("success", "Сеанс успешно добавлен");
            return "redirect:/sessions";
        } catch (Exception e) {
            // Логируем ошибку для отладки
            System.err.println("❌ Ошибка при сохранении сеанса: " + e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("error", "Ошибка при сохранении сеанса: " + e.getMessage());
            model.addAttribute("session", session);
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("halls", hallService.getAllHalls());
            return "sessions/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return sessionService.getSessionById(id)
                .map(session -> {
                    model.addAttribute("session", session);
                    model.addAttribute("movies", movieService.getAllMovies());
                    model.addAttribute("halls", hallService.getAllHalls());
                    return "sessions/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Сеанс не найден");
                    return "redirect:/sessions";
                });
    }

    @PostMapping("/{id}")
    public String updateSession(@PathVariable Long id, @Valid @ModelAttribute Session session,
                               @RequestParam("movieId") Long movieId,
                               @RequestParam("hallId") Long hallId,
                               @RequestParam("startTime") String startTimeStr,
                               BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("halls", hallService.getAllHalls());
            return "sessions/form";
        }
        
        try {
            // Парсим дату из формата datetime-local (yyyy-MM-ddTHH:mm)
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, fmt);
            
            session.setId(id);
            session.setMovie(movieService.getMovieById(movieId)
                    .orElseThrow(() -> new IllegalArgumentException("Фильм не найден")));
            session.setHall(hallService.getHallById(hallId)
                    .orElseThrow(() -> new IllegalArgumentException("Зал не найден")));
            session.setStartTime(startTime);
            sessionService.updateSession(session);
            redirectAttributes.addFlashAttribute("success", "Сеанс успешно обновлен");
            return "redirect:/sessions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("halls", hallService.getAllHalls());
            return "sessions/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteSession(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            sessionService.deleteSession(id);
            redirectAttributes.addFlashAttribute("success", "Сеанс успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/sessions";
    }
}

