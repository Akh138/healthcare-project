package com.healthcare.noteservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages") // Sera stocké dans une collection à part
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private String id;

    private Long patientId;    // À quel dossier de patient appartient cette discussion ?
    private String senderName; // Nom de celui qui parle (le médecin ou le patient)
    private String content;    // Le texte du message
    private LocalDateTime timestamp = LocalDateTime.now(); // Date et heure d'envoi
}