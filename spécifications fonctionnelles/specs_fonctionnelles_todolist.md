# Spécifications fonctionnelles — TodoList

## 1. Présentation du projet

TodoList est une application web de gestion de tâches développée avec Spring Boot et Thymeleaf. Elle permet à un utilisateur connecté de gérer ses tâches personnelles (ajout, modification, suppression, recherche, filtrage par catégorie). Un visiteur non authentifié peut consulter la page d'accueil avec des tâches fictives de démonstration.

---

## 2. Expressions des besoins

L'utilisateur souhaite disposer d'une application simple lui permettant d'organiser ses tâches quotidiennes. Il doit pouvoir créer des tâches avec un nom, une date d'échéance, une description et une catégorie optionnelle. Il doit également pouvoir modifier le statut d'une tâche (À faire, En cours, Terminée), supprimer une tâche et effectuer des recherches. Un visiteur non connecté doit pouvoir consulter la page d'accueil présentant l'application avec quelques tâches fictives en exemple.

---

## 3. Rôles et droits

| Fonctionnalité                        | Visiteur | Utilisateur connecté |
|---------------------------------------|:--------:|:--------------------:|
| Voir la page d'accueil                | OK        | OK                   |
| Consulter les tâches fictives         | OK        | OK                    |
| S'inscrire / se connecter             | OK        | N                    |
| Se déconnecter                        | N        | OK                    |
| Voir la liste de ses tâches           | N        | OK                    |
| Ajouter une tâche                     | N        | OK                    |
| Modifier une tâche                    | N        | OK                    |
| Supprimer une tâche                   | N        | OK                    |
| Rechercher une tâche                  | N        | OK                    |
| Filtrer par catégorie                 | N        | OK                    |

---

## 4. Spécifications fonctionnelles détaillées

### 4.1 Page d'accueil (visiteur & utilisateur)

- Affichage d'un titre et d'une description de l'application
- Présentation d'un ensemble de tâches fictives à titre d'exemple
- Boutons d'accès à la connexion et à l'inscription
- Navigation vers la liste des tâches si l'utilisateur est connecté

### 4.2 Inscription

- Formulaire avec : nom d'utilisateur, email, mot de passe, confirmation du mot de passe
- Validation : email unique, mot de passe de 8 caractères minimum, champs obligatoires
- En cas de succès : redirection vers la page de connexion
- En cas d'erreur : affichage du message d'erreur sous le champ concerné

### 4.3 Connexion

- Formulaire avec : email, mot de passe
- Gestion de la session utilisateur via `HttpSession`
- En cas de succès : redirection vers `/tasks`
- En cas d'erreur : message "Identifiants incorrects"

### 4.4 Liste des tâches

- Affichage de toutes les tâches de l'utilisateur connecté
- Chaque tâche affiche : nom, date d'échéance, catégorie, statut
- Actions disponibles par tâche : modifier, supprimer, changer le statut

### 4.5 Ajouter une tâche

- Formulaire avec : nom (obligatoire), description, date d'échéance, catégorie (optionnelle)
- Le statut est initialisé à `TODO` par défaut
- Validation : nom obligatoire, date au format valide
- En cas de succès : redirection vers la liste des tâches

### 4.6 Modifier une tâche

- Formulaire pré-rempli avec les données existantes
- Tous les champs modifiables : nom, description, date, catégorie, statut
- Validation identique à l'ajout
- En cas de succès : redirection vers la liste des tâches

### 4.7 Supprimer une tâche

- Bouton de suppression avec confirmation (message de confirmation)
- Suppression définitive en base de données
- Redirection vers la liste des tâches après suppression

### 4.8 Rechercher une tâche

- Barre de recherche disponible sur la page de liste
- Recherche par mot-clé sur le nom et la description de la tâche
- Résultats affichés en temps réel dans la liste (filtrage côté serveur)

### 4.9 Filtrer par catégorie

- Menu déroulant ou liste de boutons pour sélectionner une catégorie
- Affichage uniquement des tâches appartenant à la catégorie sélectionnée
- Option "Toutes les catégories" pour réinitialiser le filtre

---

## 5. Modèle de données

### Entité `Task`

| Champ         | Type          | Contrainte              |
|---------------|---------------|-------------------------|
| id            | Long          | PK, auto-généré         |
| name          | String        | Obligatoire, max 100    |
| description   | String        | Optionnel, max 500      |
| dueDate       | LocalDate     | Optionnel               |
| status        | Status (enum) | Obligatoire, défaut TODO|
| fictive       | boolean       | Défaut false            |
| user          | User          | FK, obligatoire         |
| category      | Category      | FK, optionnel           |

### Entité `User`

| Champ     | Type   | Contrainte               |
|-----------|--------|--------------------------|
| id        | Long   | PK, auto-généré          |
| username  | String | Obligatoire, unique      |
| email     | String | Obligatoire, unique      |
| password  | String | Obligatoire, hashé       |
| role      | String | Défaut "USER"            |

### Entité `Category`

| Champ | Type   | Contrainte          |
|-------|--------|---------------------|
| id    | Long   | PK, auto-généré     |
| name  | String | Obligatoire, max 50 |
| color | String | Code hex, optionnel |

### Enumération `Status`

```
TODO | IN_PROGRESS | DONE
```

---

## 6. Architecture en couches

```
src/main/java/com/fms/
├── entity/
│   ├── User.java
│   ├── Task.java
│   ├── Category.java
│   └── Status.java (enum)
├── repository/
│   ├── UserRepository.java
│   ├── TaskRepository.java
│   └── CategoryRepository.java
├── service/
│   ├── UserService.java
│   └── TaskService.java
├── controller/
│   ├── HomeController.java
│   ├── AuthController.java
│   └── TaskController.java
└── exception/
    └── TaskNotFoundException.java
```

---

## 7. Routes principales

| Méthode | URL                        | Description                        | Accès        |
|---------|----------------------------|------------------------------------|--------------|
| GET     | `/`                        | Page d'accueil                     | Public       |
| GET     | `/login`                   | Formulaire de connexion            | Public       |
| POST    | `/login`                   | Traitement connexion               | Public       |
| GET     | `/register`                | Formulaire d'inscription           | Public       |
| POST    | `/register`                | Traitement inscription             | Public       |
| GET     | `/logout`                  | Déconnexion                        | Connecté     |
| GET     | `/tasks`                   | Liste des tâches                   | Connecté     |
| GET     | `/tasks/new`               | Formulaire ajout tâche             | Connecté     |
| POST    | `/tasks`                   | Sauvegarder une nouvelle tâche     | Connecté     |
| GET     | `/tasks/{id}/edit`         | Formulaire modification            | Connecté     |
| POST    | `/tasks/{id}`              | Sauvegarder la modification        | Connecté     |
| POST    | `/tasks/{id}/delete`       | Supprimer une tâche                | Connecté     |
| POST    | `/tasks/{id}/status`       | Changer le statut                  | Connecté     |
| GET     | `/tasks?search=...`        | Rechercher une tâche               | Connecté     |
| GET     | `/tasks?category=...`      | Filtrer par catégorie              | Connecté     |

---

## 8. Spécifications techniques

| Technologie         | Version   |
|---------------------|-----------|
| Java                | 17        |
| Spring Boot         | 3.5.13    |
| Spring Data JPA     | inclus    |
| Thymeleaf           | inclus    |
| MariaDB             | 10.x      |
| Lombok              | inclus    |
| Maven               | 3.x       |
| Bootstrap (CSS)     | 5.3.x     |



