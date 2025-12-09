package ru.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cinema.model.Session;
import ru.cinema.model.Ticket;
import ru.cinema.model.User;
import ru.cinema.repository.SessionRepository;
import ru.cinema.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, SessionRepository sessionRepository) {
        this.ticketRepository = ticketRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket purchaseTicket(Ticket ticket) {
        Session session = ticket.getSession();
        if (session == null || session.getId() == null) {
            throw new IllegalArgumentException("Сеанс не указан");
        }
        
        session = sessionRepository.findById(session.getId())
                .orElseThrow(() -> new IllegalArgumentException("Сеанс не найден"));
        
        // Проверка доступности места
        List<Ticket> existingTickets = ticketRepository.findBySessionAndSeatNumber(
                session, ticket.getSeatNumber());
        boolean seatTaken = existingTickets.stream()
                .anyMatch(t -> "ACTIVE".equals(t.getStatus()));
        
        if (seatTaken) {
            throw new IllegalArgumentException("Место уже занято");
        }
        
        if (session.getAvailableSeats() == null || session.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("Нет доступных мест");
        }
        
        // Уменьшаем количество доступных мест
        session.setAvailableSeats(session.getAvailableSeats() - 1);
        sessionRepository.save(session);
        
        ticket.setPrice(session.getPrice());
        ticket.setStatus("ACTIVE");
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Ticket ticket) {
        if (!ticketRepository.existsById(ticket.getId())) {
            throw new IllegalArgumentException("Билет не найден");
        }
        return ticketRepository.save(ticket);
    }

    public void cancelTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Билет не найден"));
        
        ticket.setStatus("CANCELLED");
        ticketRepository.save(ticket);
        
        // Возвращаем место
        Session session = ticket.getSession();
        if (session != null) {
            session.setAvailableSeats(session.getAvailableSeats() + 1);
            sessionRepository.save(session);
        }
    }

    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new IllegalArgumentException("Билет не найден");
        }
        ticketRepository.deleteById(id);
    }

    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByUser(user);
    }

    public List<Ticket> getTicketsBySession(Session session) {
        return ticketRepository.findBySession(session);
    }

    public List<Ticket> getTicketsByDateRange(LocalDateTime start, LocalDateTime end) {
        return ticketRepository.findByPurchaseTimeBetween(start, end);
    }
}

