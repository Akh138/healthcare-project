package com.healthcare.noteservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notes") // C'est ici qu'on définit le nom de la collection NoSQL
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id // L'ID sera généré automatiquement par MongoDB sous forme de String (ex: 64b7...)
    private String id;

    private Long patientId;    // Le lien vers  patient dans MySQL
    private String doctorName; // Le médecin qui écrit la note
    private String content;    // Le texte de la consultation
    private LocalDateTime date = LocalDateTime.now(); // Date de la note
}