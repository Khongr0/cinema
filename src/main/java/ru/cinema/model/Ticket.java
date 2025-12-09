package ru.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Сеанс обязателен")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "Номер места обязателен")
    @Min(value = 1, message = "Номер места должен быть больше 0")
    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @NotNull(message = "Цена обязательна")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    @Column(nullable = false)
    private Double price;

    @Column(name = "purchase_time")
    private LocalDateTime purchaseTime;

    @Column(name = "status")
    private String status = "ACTIVE"; // ACTIVE, CANCELLED, USED

    @PrePersist
    protected void onCreate() {
        if (purchaseTime == null) {
            purchaseTime = LocalDateTime.now();
        }
    }

    // Constructors
    public Ticket() {}

    public Ticket(Session session, User user, Integer seatNumber, Double price) {
        this.session = session;
        this.user = user;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

