package ru.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Фильм обязателен")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @NotNull(message = "Зал обязателен")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @NotNull(message = "Время начала обязательно")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "Цена обязательна")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    @Column(nullable = false)
    private Double price;

    @Column(name = "available_seats")
    private Integer availableSeats;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    // Constructors
    public Session() {}

    public Session(Movie movie, Hall hall, LocalDateTime startTime, Double price) {
        this.movie = movie;
        this.hall = hall;
        this.startTime = startTime;
        this.price = price;
        this.availableSeats = hall != null ? hall.getCapacity() : 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
        if (hall != null && this.availableSeats == null) {
            this.availableSeats = hall.getCapacity();
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public LocalDateTime getEndTime() {
        if (movie != null && startTime != null) {
            return startTime.plusMinutes(movie.getDuration());
        }
        return null;
    }
}

