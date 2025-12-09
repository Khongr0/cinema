package ru.cinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cinema.model.Movie;
import ru.cinema.service.MovieService;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public String listMovies(@RequestParam(required = false) String search,
                            @RequestParam(required = false) String sort,
                            Model model) {
        List<Movie> movies;
        
        if (search != null && !search.isEmpty()) {
            movies = movieService.searchMovies(search);
            model.addAttribute("search", search);
        } else if ("title".equals(sort)) {
            movies = movieService.sortMoviesByTitle();
            model.addAttribute("sort", sort);
        } else if ("year".equals(sort)) {
            movies = movieService.sortMoviesByYear();
            model.addAttribute("sort", sort);
        } else {
            movies = movieService.getAllMovies();
        }
        
        model.addAttribute("movies", movies);
        return "movies/list";
    }

    @GetMapping("/new")
    public String showMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "movies/form";
    }

    @PostMapping
    public String saveMovie(@Valid @ModelAttribute Movie movie, BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "movies/form";
        }
        
        try {
            movieService.saveMovie(movie);
            redirectAttributes.addFlashAttribute("success", "Фильм успешно добавлен");
            return "redirect:/movies";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            return "redirect:/movies/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return movieService.getMovieById(id)
                .map(movie -> {
                    model.addAttribute("movie", movie);
                    return "movies/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Фильм не найден");
                    return "redirect:/movies";
                });
    }

    @PostMapping("/{id}")
    public String updateMovie(@PathVariable Long id, @Valid @ModelAttribute Movie movie,
                             BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "movies/form";
        }
        
        try {
            movie.setId(id);
            movieService.updateMovie(movie);
            redirectAttributes.addFlashAttribute("success", "Фильм успешно обновлен");
            return "redirect:/movies";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            return "redirect:/movies/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            movieService.deleteMovie(id);
            redirectAttributes.addFlashAttribute("success", "Фильм успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/movies";
    }
}

