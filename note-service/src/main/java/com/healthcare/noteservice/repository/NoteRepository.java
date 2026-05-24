package com.healthcare.noteservice.repository;

import com.healthcare.noteservice.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    // On veut pouvoir récupérer toutes les notes d'un seul patient
    List<Note> findByPatientId(Long patientId);
}