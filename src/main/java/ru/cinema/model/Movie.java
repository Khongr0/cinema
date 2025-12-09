package ru.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название фильма обязательно")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Описание обязательно")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Жанр обязателен")
    private String genre;

    @NotNull(message = "Длительность обязательна")
    @Min(value = 1, message = "Длительность должна быть больше 0")
    @Column(nullable = false)
    private Integer duration; // в минутах

    @NotNull(message = "Год выпуска обязателен")
    @Column(nullable = false)
    private Integer releaseYear;

    @NotBlank(message = "Режиссер обязателен")
    private String director;

    @Column(name = "age_rating")
    private String ageRating; // 0+, 6+, 12+, 16+, 18+

    @Column(name = "poster_url")
    private String posterUrl;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions;

    // Constructors
    public Movie() {}

    public Movie(String title, String description, String genre, Integer duration, 
                 Integer releaseYear, String director, String ageRating) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.director = director;
        this.ageRating = ageRating;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}

