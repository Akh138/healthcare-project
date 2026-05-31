# Healthcare - Gestion Médicale & Dossiers Patients

Ce projet consiste en la création d'une application de suivi médical construite sur une architecture microservices. J'ai conçu ce système pour séparer les domaines métier (Utilisateurs, Patients, Notes) afin d'obtenir un système scalable, sécurisé et entièrement conteneurisé.

## Architecture du système

L'application est composée de 5 microservices qui communiquent au sein d'un réseau Docker :

*   **Portail UI (8080)** : Interface web interactive développée sous Thymeleaf et Bootstrap.
*   **Config Server (8888)** : Gestion centralisée des configurations via un dépôt GitHub distant.
*   **Consul (8500)** : Annuaire pour la découverte automatique des services et le Healthcheck.
*   **Services Métier** : User-Service (MySQL), Patient-Service (MySQL) et Note-Service (MongoDB).
*   **Bases de données** : Utilisation mixte de MySQL (SQL) et MongoDB (NoSQL).

## Stack Technique

Pour ce projet, j'ai choisi des outils modernes pour garantir la solidité du système :

*   **Java 17 & Spring Boot 3** : Utilisation de Jakarta EE pour la persistance.
*   **OpenFeign** : Communication typée et simplifiée entre les microservices.
*   **Spring Security 6** : Gestion des accès par rôles (RBAC) et cryptage BCrypt.
*   **Docker & Compose** : Conteneurisation de chaque brique pour un déploiement facile.
*   **Double Persistance** : MySQL pour les données structurées et MongoDB pour la flexibilité des notes de consultation.

## Qualité & Tests

Le projet a été développé avec une exigence de fiabilité maximale. Chaque microservice métier possède une **couverture de code de 100%**.

*   **Tests Unitaires** : Utilisation de **JUnit 5** et **Mockito** pour tester la logique métier en isolant les services.
*   **Tests de Controllers** : Validation des points d'entrée (Endpoints) avec **MockMvc** pour garantir le respect des contrats d'interface (Codes HTTP, formats JSON).
*   **Tests d'Intégration** : Vérification de la chaîne complète (Controller -> Service -> Repository) en utilisant des bases de données de test en mémoire :
    *   **H2** pour les services MySQL.
    *   **Embedded MongoDB** pour le service de notes NoSQL.

## Gestion des Exceptions

Pour garantir une expérience utilisateur fluide et des APIs professionnelles, j'ai mis en place :
*   **GlobalExceptionHandler** : Un trieur d'erreurs centralisé qui attrape les exceptions et les transforme en messages JSON clairs.
*   **Custom Exceptions** : Création d'erreurs métier spécifiques comme `ResourceNotFoundException`.
*   **UI Error Pages** : Intégration de pages d'erreurs **404** (introuvable) et **500** (serveur) personnalisées avec le design de l'application.

## Comment lancer le projet ?

### Pré-requis
*   **Docker & Docker Compose**
*   **Java 17** et **Maven** (pour la compilation)

### Installation rapide

1.  **Cloner le projet** :
    ```bash
    git clone https://github.com/Akh138/healthcare-project.git
    ```

2.  **Compiler tous les services** (pour générer les fichiers .jar) :
    Exécuter `./mvnw clean package -DskipTests` dans chaque dossier de microservice.

3.  **Lancer l'infrastructure complète avec Docker** :
    ```bash
    docker-compose up --build -d
    ```

### Accès au système
*   **Interface Utilisateur** : http://localhost:8080
*   **Annuaire des services (Consul)** : http://localhost:8500
*   **Identifiants Admin par défaut** : admin / 12345