package ru.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cinema.model.Movie;
import ru.cinema.repository.MovieRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Movie movie) {
        if (!movieRepository.existsById(movie.getId())) {
            throw new IllegalArgumentException("Фильм не найден");
        }
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new IllegalArgumentException("Фильм не найден");
        }
        movieRepository.deleteById(id);
    }

    public List<Movie> searchMovies(String keyword) {
        return movieRepository.searchMovies(keyword);
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreContainingIgnoreCase(genre);
    }

    public List<Movie> getMoviesByYear(Integer year) {
        return movieRepository.findByReleaseYear(year);
    }

    public List<Movie> sortMoviesByTitle() {
        return movieRepository.findAllByOrderByTitleAsc();
    }

    public List<Movie> sortMoviesByYear() {
        return movieRepository.findAllByOrderByReleaseYearDesc();
    }
}

