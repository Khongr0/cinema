package ru.cinema.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.cinema.model.Role;
import ru.cinema.repository.UserRepository;

@Controller
public class HomeController {
    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            userRepository.findByUsername(username).ifPresent(user -> {
                model.addAttribute("currentUser", user);
                model.addAttribute("isAdmin", user.getRole() == Role.ADMIN);
                model.addAttribute("isCashier", user.getRole() == Role.CASHIER || user.getRole() == Role.ADMIN);
            });
        }
        return "home";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}

