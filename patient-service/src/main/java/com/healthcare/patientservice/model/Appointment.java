package com.healthcare.patientservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;      // L'ID du patient (pour le lien SQL)
    private String patientName;   // Le nom du patient (pour l'affichage rapide)
    private String doctorUsername; // Le nom du médecin qui a le RDV
    private LocalDateTime dateTime; // Date et Heure
    private String reason;         // Motif (ex: Consultation, Urgence)
}