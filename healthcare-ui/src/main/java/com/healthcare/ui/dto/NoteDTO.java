package com.healthcare.ui.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * CET OBJET REPRÉSENTE UNE NOTE MÉDICALE.
 * Attention : Ici l'ID est un String car les données viennent de MongoDB !
 */
@Data
public class NoteDTO {
    // L'identifiant généré par MongoDB (ex: "65a12b...")
    private String id;

    // Le lien vers le patient (ID MySQL) pour savoir à qui appartient la note
    private Long patientId;

    // Le nom du docteur qui a écrit la note
    private String doctorName;

    // Le contenu de la consultation
    private String content;

    // La date et l'heure de création de la note
    private LocalDateTime date;
}