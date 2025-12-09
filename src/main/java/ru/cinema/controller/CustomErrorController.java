package ru.cinema.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            try {
                Integer statusCode = Integer.valueOf(status.toString());
                
                if (statusCode == HttpStatus.NOT_FOUND.value()) {
                    model.addAttribute("errorCode", "404");
                    model.addAttribute("errorMessage", "Страница не найдена");
                    model.addAttribute("errorDescription", "Запрашиваемая страница не существует.");
                } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                    model.addAttribute("errorCode", "500");
                    model.addAttribute("errorMessage", "Внутренняя ошибка сервера");
                    model.addAttribute("errorDescription", "Произошла ошибка при обработке запроса.");
                } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                    model.addAttribute("errorCode", "403");
                    model.addAttribute("errorMessage", "Доступ запрещен");
                    model.addAttribute("errorDescription", "У вас нет доступа к этой странице.");
                } else {
                    model.addAttribute("errorCode", statusCode);
                    model.addAttribute("errorMessage", "Произошла ошибка");
                    model.addAttribute("errorDescription", "Попробуйте позже или обратитесь к администратору.");
                }
            } catch (NumberFormatException e) {
                model.addAttribute("errorCode", "Ошибка");
                model.addAttribute("errorMessage", "Произошла неизвестная ошибка");
                model.addAttribute("errorDescription", "Попробуйте позже или обратитесь к администратору.");
            }
        } else {
            model.addAttribute("errorCode", "Ошибка");
            model.addAttribute("errorMessage", "Произошла неизвестная ошибка");
            model.addAttribute("errorDescription", "Попробуйте позже или обратитесь к администратору.");
        }
        
        return "error";
    }
}

