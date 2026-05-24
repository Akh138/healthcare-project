package com.healthcare.noteservice.repository;

import com.healthcare.noteservice.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    // Récupérer les messages d'un patient triés par date (pour faire un chat)
    List<Message> findByPatientIdOrderByTimestampAsc(Long patientId);
}