package com.healthcare.ui.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NoteDTO {
    private String id;
    private Long patientId;
    private String doctorName;
    private String content;
    private LocalDateTime date;
}