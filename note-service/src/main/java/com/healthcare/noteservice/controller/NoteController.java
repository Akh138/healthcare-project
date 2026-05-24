package com.healthcare.noteservice.controller;

import com.healthcare.noteservice.model.Note;
import com.healthcare.noteservice.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    // Enregistrer une nouvelle note de consultation
    @PostMapping
    public Note createNote(@RequestBody Note note) {
        return noteService.saveNote(note);
    }

    // Récupérer tout l'historique d'un patient
    @GetMapping("/patient/{patientId}")
    public List<Note> getNotesByPatient(@PathVariable Long patientId) {
        return noteService.getNotesByPatient(patientId);
    }

    // Supprimer une note
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
    }
}