package com.healthcare.ui.dto;

import lombok.Data;

/**
 * CET OBJET SERT À GÉRER LES COMPTES UTILISATEURS.
 * On l'utilise pour l'inscription (Signup) et la connexion (Login).
 */
@Data
public class UserDTO {
    // L'identifiant du compte dans la base MySQL du user-service
    private Long id;

    // Le pseudo unique choisi par l'utilisateur
    private String username;

    // Le mot de passe (qui sera encodé en BCrypt avant d'être sauvegardé)
    private String password;

    // Le rôle pour savoir ce que l'utilisateur a le droit de faire ("DOCTOR", "ADMIN", "PATIENT")
    private String role;
}