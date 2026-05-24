package com.healthcare.noteservice.service;

import com.healthcare.noteservice.model.Note;
import com.healthcare.noteservice.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;


    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    public List<Note> getNotesByPatient(Long patientId) {
        return noteRepository.findByPatientId(patientId);
    }

    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }
}