package ru.cinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cinema.model.Role;
import ru.cinema.model.User;
import ru.cinema.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listUsers(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("users", userService.searchUsers(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("users", userService.getAllUsers());
        }
        return "admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String showUserEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return userService.getUserById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    return "admin/user-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Пользователь не найден");
                    return "redirect:/admin/users";
                });
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user,
                           @RequestParam("role") String roleStr,
                           @RequestParam(value = "password", required = false) String password,
                           RedirectAttributes redirectAttributes) {
        try {
            user.setId(id);
            user.setRole(Role.valueOf(roleStr));
            // Если пароль не указан или пустой, установить null, чтобы сохранить старый
            if (password == null || password.isEmpty()) {
                user.setPassword(null);
            }
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}

