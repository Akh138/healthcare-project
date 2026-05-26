package com.healthcare.noteservice.service;

import com.healthcare.noteservice.model.Message;
import com.healthcare.noteservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    /**
     * C'EST ICI QU'ON ENREGISTRE UN NOUVEAU MESSAGE DANS MONGODB
     */
    public Message saveMessage(Message message) {
        // SÉCURITÉ : Comme pour les notes, on vide l'ID avant de sauvegarder.
        // Ça oblige MongoDB à créer un nouvel ID unique (ex: "65f82...")
        // au lieu d'utiliser l'ID du patient et d'écraser l'ancien message.
        message.setId(null);

        // On enregistre l'heure précise de l'envoi pour que la discussion soit bien rangée
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    /**
     * POUR RÉCUPÉRER TOUTE LA CONVERSATION D'UN PATIENT
     * On trie par "Timestamp" (du plus vieux au plus récent)
     */
    public List<Message> getMessagesByPatient(Long patientId) {
        return messageRepository.findByPatientIdOrderByTimestampAsc(patientId);
    }
}