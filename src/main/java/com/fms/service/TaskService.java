package com.fms.service;

import com.fms.entity.*;
import com.fms.exception.TaskNotFoundException;
import com.fms.repository.CategoryRepository;
import com.fms.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Retourne toutes les tâches de l'utilisateur connecté
     */
    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }

    /**
     * Recherche par mot-clé dans le nom ou la description
     */
    public List<Task> search(User user, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return taskRepository.findByUser(user);
        }
        return taskRepository
            .findByUserAndNameContainingIgnoreCaseOrUserAndDescriptionContainingIgnoreCase(
                user, keyword, user, keyword
            );
    }

    /**
     * Filtre les tâches par catégorie
     */
    public List<Task> filterByCategory(User user, Long categoryId) {
        if (categoryId == null) {
            return taskRepository.findByUser(user);
        }
        return taskRepository.findByUserAndCategoryId(user, categoryId);
    }


    /**
     * Retourne les tâches fictives pour la page d'accueil visiteur
     */
    public List<Task> getFictiveTasks() {
        return taskRepository.findByFictiveTrue();
    }

    /**
     * Crée une nouvelle tâche pour l'utilisateur connecté
     */
    public Task createTask(User user, String name, String description,
                           LocalDate dueDate, Long categoryId) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setStatus(Status.TODO);
        task.setUser(user);
        task.setFictive(false);

        if (categoryId != null) {
            categoryRepository.findById(categoryId)
                .ifPresent(task::setCategory);
        }

        return taskRepository.save(task);
    }

    /**
     * Met à jour une tâche existante
     */
    public Task updateTask(Long taskId, String name, String description,
                           LocalDate dueDate, Status status, Long categoryId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        task.setName(name);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setStatus(status);

        if (categoryId != null) {
            categoryRepository.findById(categoryId)
                .ifPresent(task::setCategory);
        } else {
            task.setCategory(null);
        }

        return taskRepository.save(task);
    }


    /**
     * Supprime une tâche
     */
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        taskRepository.deleteById(taskId);
    }

    /**
     * Retourne toutes les catégories disponibles
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}