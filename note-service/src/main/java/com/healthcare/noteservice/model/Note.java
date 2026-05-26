package com.healthcare.noteservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notes") // C'est le nom du dossier dans MongoDB
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id // MongoDB va créer un ID unique automatiquement (ex: 65a12b...)
    private String id;

    private Long patientId;    // L'ID du patient qui vient de MySQL
    private String doctorName; // Le nom du médecin qui écrit
    private String content;    // Ce qui a été dit pendant le rendez-vous

    // Je mets la date par défaut à "maintenant"
    private LocalDateTime date = LocalDateTime.now();
}