package com.fms.web;

import com.fms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final TaskService taskService;

    /**
     * Page d'accueil : affiche les tâches fictives pour le visiteur
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tasks", taskService.getFictiveTasks());
        return "home";
    }
}