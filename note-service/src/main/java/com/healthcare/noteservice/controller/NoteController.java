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

    // Route pour créer une note (POST)
    @PostMapping
    public Note createNote(@RequestBody Note note) {
        // On appelle le service qui va gérer l'ID automatiquement
        return noteService.saveNote(note);
    }

    // Route pour récupérer l'historique d'un patient
    @GetMapping("/patient/{patientId}")
    public List<Note> getNotesByPatient(@PathVariable Long patientId) {
        return noteService.getNotesByPatient(patientId);
    }

    // Route pour supprimer une note par son ID NoSQL
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
    }
}