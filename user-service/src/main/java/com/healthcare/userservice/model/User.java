package com.healthcare.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * ENTITÉ USER (Table MySQL)
 * C'est ici qu'on définit les comptes pour la sécurité (Admins, Médecins, Patients).
 */
@Entity
@Table(name = "users")
@Data // Génère automatiquement les Getters/Setters
@NoArgsConstructor // Constructeur vide obligatoire pour JPA
@AllArgsConstructor // Constructeur avec tous les champs
@Builder // TOUCHE PRO : Permet de créer un utilisateur proprement dans le code
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LE PSEUDO : Il doit être unique (pas deux fois le même pseudo en base)
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Column(unique = true, nullable = false)
    private String username;

    // LE MOT DE PASSE : Il sera stocké sous forme cryptée (BCrypt)
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Column(nullable = false)
    private String password;

    // LE RÔLE : Pour savoir qui a accès à quoi ("ADMIN", "DOCTOR", "PATIENT")
    @NotBlank(message = "Le rôle est obligatoire")
    @Column(nullable = false)
    private String role;
}