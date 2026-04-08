package com.fms.repository;

// import com.fms.entity.Status;
import com.fms.entity.Task;
import com.fms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Toutes les tâches d'un utilisateur
    List<Task> findByUser(User user);

    // Recherche par mot-clé (nom ou description)
    List<Task> findByUserAndNameContainingIgnoreCaseOrUserAndDescriptionContainingIgnoreCase(
        User user1, String name,
        User user2, String description
    );

    // Filtre par catégorie
    List<Task> findByUserAndCategoryId(User user, Long categoryId);

    // Tâches fictives pour la page d'accueil
    List<Task> findByFictiveTrue();
}