package com.healthcare.noteservice.service;

import com.healthcare.noteservice.model.Message;
import com.healthcare.noteservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Enregistrer un nouveau message
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    // Récupérer la discussion complète d'un patient
    public List<Message> getMessagesByPatient(Long patientId) {
        return messageRepository.findByPatientIdOrderByTimestampAsc(patientId);
    }
}