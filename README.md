# SpringEval — Gestionnaire de tâches

Application web de gestion de tâches développée avec Spring Boot pour Evaluation

## Stack technique

| Couche | Technologie |
|---|---|
| Backend | Spring Boot 3.5.13, Java 17 |
| Frontend | Thymeleaf, Thymeleaf Layout Dialect, Bootstrap |
| Persistance | Spring Data JPA, Hibernate |
| Base de données | MariaDB |
| Outillage | Lombok, Maven |

## Fonctionnalités

- Création, modification et suppression de tâches
- Statuts : `TODO`, `IN_PROGRESS`, `DONE`
- Catégorisation des tâches avec code couleur
- Filtrage par mot-clé et par catégorie
- Page d'accueil avec tâches fictives de démonstration
- Inscription et connexion utilisateur
- Données de démarrage injectées via `CommandLineRunner`

## Structure du projet

```
src/main/java/com/fms/
├── entity/          # Entités JPA : User, Task, Category, Status
├── repository/      # Interfaces Spring Data : TaskRepository, UserRepository, CategoryRepository
├── service/         # Logique métier : TaskService, UserService
├── web/             # Controllers : HomeController, TaskController, AuthController
├── exception/       # TaskNotFoundException
└── SpringMvc/       # Point d'entrée : SpringMvcApplication
```

## Prérequis

- Java 17+
- MariaDB démarré sur le port `4000`
- Maven

## Installation

```bash
# Cloner le projet
git clone https://github.com/ton-compte/SpringEval.git
cd SpringEval
```

Créer la base de données (optionnel — la propriété `createDatabaseIfNotExist=true` la crée automatiquement) :

```sql
CREATE DATABASE SpringEval;
```

Configurer `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mariadb://localhost:4000/SpringEval?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=ton_mot_de_passe
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

Lancer l'application :

```bash
mvn spring-boot:run
```

L'application est accessible sur [http://localhost:8080](http://localhost:8080).

Au premier démarrage, le `CommandLineRunner` insère automatiquement :
- Un utilisateur `demo` (tâches fictives de la homepage)
- Un compte administrateur `marty`
- 3 tâches de démonstration avec catégories

## Modèle de données

```
User ──< Task >── Category
         │
        Status (enum) : TODO | IN_PROGRESS | DONE
```

Un utilisateur possède plusieurs tâches. Chaque tâche appartient à une catégorie et a un statut. Le champ `fictive` distingue les tâches de démonstration des vraies tâches utilisateur.

## Compte de test

| Champ | Valeur |
|---|---|
| Utilisateur | `marty` |
| Email | `marty@fms.com` |
| Rôle | `ADMIN` |

## À venir

- [ ] Spring Security avec encodage BCrypt des mots de passe
- [ ] Vue kanban par statut

## Auteur

Marty RABORD
