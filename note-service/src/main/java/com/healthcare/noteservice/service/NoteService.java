package com.healthcare.noteservice.service;

import com.healthcare.noteservice.model.Note;
import com.healthcare.noteservice.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    // C'est ici qu'on enregistre la note dans MongoDB
    public Note saveNote(Note note) {
        // SÉCURITÉ : On force l'ID à null.
        // Comme ça, on est SÛR que MongoDB va créer une NOUVELLE note
        // et qu'il ne va pas remplacer une ancienne par erreur.
        note.setId(null);

        // On s'assure aussi que la date est bien celle du moment présent
        note.setDate(LocalDateTime.now());

        return noteRepository.save(note);
    }

    // Pour retrouver toutes les notes d'un patient précis
    public List<Note> getNotesByPatient(Long patientId) {
        return noteRepository.findByPatientId(patientId);
    }

    // Pour supprimer une note si on s'est trompé
    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }
}