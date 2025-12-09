package ru.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cinema.model.Session;
import ru.cinema.model.Ticket;
import ru.cinema.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser(User user);
    List<Ticket> findBySession(Session session);
    List<Ticket> findBySessionAndSeatNumber(Session session, Integer seatNumber);
    List<Ticket> findByPurchaseTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Ticket> findByStatus(String status);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.purchaseTime BETWEEN :start AND :end")
    Long countTicketsByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT AVG(TIMESTAMPDIFF(MINUTE, t.purchaseTime, s.startTime)) FROM Ticket t JOIN t.session s WHERE t.purchaseTime BETWEEN :start AND :end")
    Double getAverageWaitingTime(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}

