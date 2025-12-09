package ru.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название зала обязательно")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "Количество мест обязательно")
    @Min(value = 1, message = "Количество мест должно быть больше 0")
    @Column(nullable = false)
    private Integer capacity;

    @NotBlank(message = "Тип зала обязателен")
    @Column(name = "hall_type")
    private String hallType; // Обычный, 3D, IMAX, VIP

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions;

    // Constructors
    public Hall() {}

    public Hall(String name, Integer capacity, String hallType, String description) {
        this.name = name;
        this.capacity = capacity;
        this.hallType = hallType;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getHallType() {
        return hallType;
    }

    public void setHallType(String hallType) {
        this.hallType = hallType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}

