package ru.cinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cinema.model.Hall;
import ru.cinema.service.HallService;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/halls")
public class HallController {
    private final HallService hallService;

    @Autowired
    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping
    public String listHalls(@RequestParam(required = false) String search,
                           @RequestParam(required = false) String sort,
                           Model model) {
        List<Hall> halls;
        
        if (search != null && !search.isEmpty()) {
            halls = hallService.searchHalls(search);
            model.addAttribute("search", search);
        } else if ("name".equals(sort)) {
            halls = hallService.sortHallsByName();
            model.addAttribute("sort", sort);
        } else if ("capacity".equals(sort)) {
            halls = hallService.sortHallsByCapacity();
            model.addAttribute("sort", sort);
        } else {
            halls = hallService.getAllHalls();
        }
        
        model.addAttribute("halls", halls);
        return "halls/list";
    }

    @GetMapping("/new")
    public String showHallForm(Model model) {
        model.addAttribute("hall", new Hall());
        return "halls/form";
    }

    @PostMapping
    public String saveHall(@Valid @ModelAttribute Hall hall, BindingResult result,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "halls/form";
        }
        
        try {
            hallService.saveHall(hall);
            redirectAttributes.addFlashAttribute("success", "Зал успешно добавлен");
            return "redirect:/halls";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            return "redirect:/halls/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return hallService.getHallById(id)
                .map(hall -> {
                    model.addAttribute("hall", hall);
                    return "halls/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Зал не найден");
                    return "redirect:/halls";
                });
    }

    @PostMapping("/{id}")
    public String updateHall(@PathVariable Long id, @Valid @ModelAttribute Hall hall,
                            BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "halls/form";
        }
        
        try {
            hall.setId(id);
            hallService.updateHall(hall);
            redirectAttributes.addFlashAttribute("success", "Зал успешно обновлен");
            return "redirect:/halls";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            return "redirect:/halls/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteHall(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            hallService.deleteHall(id);
            redirectAttributes.addFlashAttribute("success", "Зал успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/halls";
    }
}

