package ru.cinema.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletRequest req, Model model, RedirectAttributes redirectAttributes) {
        // Log minimal info to console for debugging
        System.err.println("Unhandled exception at " + req.getRequestURI() + ": " + ex.getMessage());
        ex.printStackTrace();

        // Add info to model to display friendly page
        model.addAttribute("errorMessage", ex.getMessage() == null ? "Внутренняя ошибка сервера" : ex.getMessage());
        model.addAttribute("requestUri", req.getRequestURI());
        return "error"; // reuse existing templates/error.html
    }
}
