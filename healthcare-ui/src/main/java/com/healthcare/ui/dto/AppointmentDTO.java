package com.healthcare.ui.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * CET OBJET SERT À GÉRER LES RENDEZ-VOUS.
 * Il fait le lien entre un patient et un créneau horaire.
 */
@Data
public class AppointmentDTO {
    // L'ID du rendez-vous (Géré par MySQL)
    private Long id;

    // Le lien vers le patient concerné
    private Long patientId;

    // On stocke le nom du patient pour l'afficher plus vite dans le planning
    private String patientName;

    // Le médecin qui reçoit le patient
    private String doctorUsername;

    // La date et l'heure du rendez-vous
    private LocalDateTime dateTime;

    // La raison de la visite (ex: "Contrôle annuel", "Urgence")
    private String reason;
}