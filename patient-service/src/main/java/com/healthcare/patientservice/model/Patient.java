package com.healthcare.patientservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * ENTITÉ PATIENT (Table MySQL)
 * C'est ici qu'on définit la structure des données dans la base de données.
 */
@Entity
@Table(name = "patients")
@Data // Génère Getters, Setters, ToString...
@NoArgsConstructor // Constructeur vide (obligatoire pour JPA)
@AllArgsConstructor // Constructeur avec tous les champs
@Builder // TOUCHE PRO : Permet de créer un patient facilement (ex: Patient.builder().name("...").build())
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- IDENTITÉ ET COORDONNÉES ---
    // Les messages seront affichés si le formulaire est mal rempli

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate birthday;

    @NotBlank(message = "Le genre est obligatoire")
    private String gender;

    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Format téléphone invalide")
    private String phone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    @Column(unique = true) // Un email ne peut pas être utilisé deux fois
    private String email;

    // --- LIENS AVEC L'APPLICATION ---

    // Pseudo du médecin qui a créé la fiche
    @Column(name = "doctor_username")
    private String doctorUsername;

    // Pseudo du compte utilisateur (si le patient veut se connecter)
    @Column(name = "patient_username")
    private String patientUsername;

    // Stockage de la photo en Base64 (on utilise LONGTEXT car c'est une grosse chaîne)
    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGTEXT")
    private String profilePicture;
}