package ru.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cinema.model.Role;
import ru.cinema.repository.TicketRepository;
import ru.cinema.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public StatisticsService(UserRepository userRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public Map<Role, Long> getUsersByRole() {
        Map<Role, Long> stats = new HashMap<>();
        for (Role role : Role.values()) {
            stats.put(role, (long) userRepository.findByRole(role).size());
        }
        return stats;
    }

    public long getTotalTickets() {
        return ticketRepository.count();
    }

    public long getTicketsByDateRange(LocalDateTime start, LocalDateTime end) {
        return ticketRepository.countTicketsByDateRange(start, end);
    }

    public Double getAverageWaitingTime(LocalDateTime start, LocalDateTime end) {
        Double avgTime = ticketRepository.getAverageWaitingTime(start, end);
        return avgTime != null ? avgTime : 0.0;
    }

    public Map<String, Object> getAllStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", getTotalUsers());
        stats.put("usersByRole", getUsersByRole());
        stats.put("totalTickets", getTotalTickets());
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minusWeeks(1);
        stats.put("ticketsLastWeek", getTicketsByDateRange(weekAgo, now));
        stats.put("averageWaitingTime", getAverageWaitingTime(weekAgo, now));
        
        return stats;
    }
}

