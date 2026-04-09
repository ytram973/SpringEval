package com.fms.web;

import com.fms.entity.Status;
import com.fms.entity.Task;
import com.fms.entity.User;
import com.fms.service.TaskService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Récupère l'utilisateur connecté depuis la session
     * Redirige vers /login si non connecté
     */
    private User getConnectedUser(HttpSession session) {
        return (User) session.getAttribute("connectedUser");
    }

    /** Liste des tâches avec recherche et filtre catégorie */
    @GetMapping
    public String list(HttpSession session,
            Model model,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long category) {
        User user = getConnectedUser(session);
        if (user == null)
            return "redirect:/login";

        List<Task> tasks;
        if (search != null && !search.isBlank()) {
            tasks = taskService.search(user, search);
        } else if (category != null) {
            tasks = taskService.filterByCategory(user, category);
        } else {
            tasks = taskService.getTasksByUser(user);
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("categories", taskService.getAllCategories());
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("statuses", Status.values());
        return "tasks/list";
    }

    /** Formulaire ajout */
    @GetMapping("/new")
    public String newForm(HttpSession session, Model model) {
        if (getConnectedUser(session) == null)
            return "redirect:/login";
        model.addAttribute("categories", taskService.getAllCategories());
        model.addAttribute("statuses", Status.values());
        return "tasks/form";
    }

    /** Sauvegarde d'une nouvelle tâche */
    @PostMapping
    public String create(HttpSession session,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String dueDate,
            @RequestParam(required = false) Long categoryId) {
        User user = getConnectedUser(session);
        if (user == null)
            return "redirect:/login";

        LocalDate date = (dueDate != null && !dueDate.isBlank())
                ? LocalDate.parse(dueDate)
                : null;

        taskService.createTask(user, name, description, date, categoryId);
        return "redirect:/tasks";
    }

    /** Formulaire modification */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
            HttpSession session,
            Model model) {
        if (getConnectedUser(session) == null)
            return "redirect:/login";
        taskService.getTasksByUser(getConnectedUser(session))
                .stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .ifPresent(t -> model.addAttribute("task", t));
        model.addAttribute("categories", taskService.getAllCategories());
        model.addAttribute("statuses", Status.values());
        return "tasks/form";
    }

    /** Sauvegarde modification */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
            HttpSession session,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String dueDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoryId) {
        if (getConnectedUser(session) == null)
            return "redirect:/login";

        LocalDate date = (dueDate != null && !dueDate.isBlank())
                ? LocalDate.parse(dueDate)
                : null;
        Status s = (status != null) ? Status.valueOf(status) : Status.TODO;

        taskService.updateTask(id, name, description, date, s, categoryId);
        return "redirect:/tasks";
    }

    /** Suppression */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (getConnectedUser(session) == null)
            return "redirect:/login";
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

}