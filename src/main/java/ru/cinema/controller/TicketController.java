package ru.cinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// ...existing imports...
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cinema.model.Role;
import ru.cinema.model.Ticket;
import ru.cinema.model.User;
import ru.cinema.repository.UserRepository;
import ru.cinema.service.SessionService;
import ru.cinema.service.TicketService;

// ...existing imports...
import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final SessionService sessionService;
    private final UserRepository userRepository;

    @Autowired
    public TicketController(TicketService ticketService, SessionService sessionService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.sessionService = sessionService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listTickets(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Ticket> tickets;
        
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            
            if (user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.CASHIER)) {
                tickets = ticketService.getAllTickets();
            } else {
                tickets = ticketService.getTicketsByUser(user);
            }
        } else {
            tickets = List.of();
        }
        
        model.addAttribute("tickets", tickets);
        return "tickets/list";
    }

    @GetMapping("/new")
    public String showTicketForm(@RequestParam(required = false) Long sessionId, Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("sessions", sessionService.getAllSessions());
        if (sessionId != null) {
            sessionService.getSessionById(sessionId).ifPresent(session -> {
                model.addAttribute("selectedSession", session);
            });
        }
        return "tickets/form";
    }

    @PostMapping
    public String purchaseTicket(@ModelAttribute Ticket ticket,
                                @RequestParam("sessionId") Long sessionId,
                                @RequestParam("seatNumber") Integer seatNumber,
                                RedirectAttributes redirectAttributes) {
        try {
            sessionService.getSessionById(sessionId).ifPresent(session -> {
                ticket.setSession(session);
                ticket.setSeatNumber(seatNumber);

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated()) {
                    userRepository.findByUsername(auth.getName()).ifPresent(ticket::setUser);
                }

                ticketService.purchaseTicket(ticket);
            });

            redirectAttributes.addFlashAttribute("success", "Билет успешно приобретен");
            return "redirect:/tickets";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            return "redirect:/tickets/new?sessionId=" + sessionId;
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelTicket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ticketService.cancelTicket(id);
            redirectAttributes.addFlashAttribute("success", "Билет успешно отменен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/tickets";
    }
}

