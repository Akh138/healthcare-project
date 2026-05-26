package com.healthcare.ui.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * CET OBJET SERT À ENVOYER DES MESSAGES ENTRE MÉDECINS ET PATIENTS.
 * Comme les notes, c'est stocké dans MongoDB.
 */
@Data
public class MessageDTO {
    // L'ID unique du message (Format String pour MongoDB)
    private String id;

    // Pour savoir dans quel dossier de patient le message doit s'afficher
    private Long patientId;

    // Le nom de celui qui envoie le message (Pseudo du docteur ou du patient)
    private String senderName;

    // Le texte du message
    private String content;

    // Le moment précis où le message a été envoyé
    private LocalDateTime timestamp;
}