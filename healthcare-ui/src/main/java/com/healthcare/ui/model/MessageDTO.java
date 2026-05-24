package com.healthcare.ui.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private String id;
    private Long patientId;
    private String senderName;
    private String content;
    private LocalDateTime timestamp;
}