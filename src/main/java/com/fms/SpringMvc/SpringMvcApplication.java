package com.fms.SpringMvc;

import java.time.LocalDate;
import com.fms.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.fms.repository.CategoryRepository;
import com.fms.repository.TaskRepository;
import com.fms.repository.UserRepository;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.fms")
@EnableJpaRepositories(basePackages = "com.fms.repository")
@EntityScan(basePackages = "com.fms.entity")

public class SpringMvcApplication implements CommandLineRunner {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringMvcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // if (userRepository.count() == 0) {

        //     // --- Catégorie ---
        //     Category dev = new Category();
        //     dev.setName("Développement");
        //     dev.setColor("#6C63FF");
        //     categoryRepository.save(dev);

        //     // --- User fictif (démo homepage) ---
        //     User demo = new User();
        //     demo.setUsername("demo");
        //     demo.setEmail("demo@fms.com");
        //     demo.setPassword("demo");
        //     demo.setRole("USER");
        //     userRepository.save(demo);

        //     // --- 3 Tasks fictives liées au user demo ---
        //     Task t1 = new Task();
        //     t1.setName("Mettre en place Spring Security");
        //     t1.setDescription("Configurer l'authentification et les rôles USER/ADMIN");
        //     t1.setDueDate(LocalDate.of(2026, 4, 15));
        //     t1.setStatus(Status.IN_PROGRESS);
        //     t1.setFictive(true);
        //     t1.setUser(demo);
        //     t1.setCategory(dev);
        //     taskRepository.save(t1);

        //     Task t2 = new Task();
        //     t2.setName("Créer le kanban Thymeleaf");
        //     t2.setDescription("Afficher les tâches par statut : TODO, IN_PROGRESS, DONE");
        //     t2.setDueDate(LocalDate.of(2026, 4, 20));
        //     t2.setStatus(Status.TODO);
        //     t2.setFictive(true);
        //     t2.setUser(demo);
        //     t2.setCategory(dev);
        //     taskRepository.save(t2);

        //     Task t3 = new Task();
        //     t3.setName("Rédiger la documentation technique");
        //     t3.setDescription("Décrire les entités, les endpoints et le schéma de la base");
        //     t3.setDueDate(LocalDate.of(2026, 4, 10));
        //     t3.setStatus(Status.DONE);
        //     t3.setFictive(true);
        //     t3.setUser(demo);
        //     t3.setCategory(dev);
        //     taskRepository.save(t3);

        //     // --- User Marty (compte réel, aucune tâche pré-assignée) ---
        //     User marty = new User();
        //     marty.setUsername("marty");
        //     marty.setEmail("marty@fms.com");
        //     marty.setPassword("admin973");
        //     marty.setRole("ADMIN");
        //     userRepository.save(marty);
        // }

    }
}