package com.healthcare.ui.dto;

import lombok.Data;

/**
 * CET OBJET EST UN "DTO" (Data Transfer Object).
 * Il sert à transporter les données du patient entre mon interface (UI)
 * et mon microservice "patient-service".
 * Ce n'est pas une entité de base de données, c'est juste un "paquet" de données.
 */
@Data // Grâce à Lombok, les Getters, Setters, et le ToString sont générés automatiquement.
public class PatientDTO {

    // L'identifiant unique du patient (géré par MySQL dans le microservice)
    private Long id;

    // Les informations d'identité classiques
    private String firstName;
    private String lastName;
    private String birthday; // Format String pour plus de simplicité lors des transferts JSON
    private String address;
    private String phone;
    private String gender;
    private String email;

    /**
     * LIENS AVEC LES COMPTES UTILISATEURS (Security)
     */

    // Le pseudo du médecin qui s'occupe de ce patient (pour filtrer la liste)
    private String doctorUsername;

    // Le pseudo du patient lui-même (s'il veut se connecter à son propre espace)
    private String patientUsername;

    // Ici on stocke la photo de profil sous forme de texte (Base64)
    // pour pouvoir l'afficher directement dans le dossier médical
    private String profilePicture;
}