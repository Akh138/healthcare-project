package com.healthcare.noteservice.service;

import com.healthcare.noteservice.model.Note;
import com.healthcare.noteservice.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TEST UNITAIRE POUR LES NOTES (MongoDB).
 * Je vérifie que mes notes de consultation sont bien enregistrées et récupérées.
 */
@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository; // Mon simulateur MongoDB

    @InjectMocks
    private NoteService noteService; // Mon vrai service NoSQL

    // --- TEST 1 : VÉRIFIER QU'ON RÉCUPÈRE LES NOTES D'UN PATIENT ---
    @Test
    public void testGetNotesByPatient_ShouldReturnList() {
        // 1. PRÉPARER
        when(noteRepository.findByPatientId(1L)).thenReturn(Arrays.asList(new Note(), new Note()));

        // 2. AGIR
        List<Note> result = noteService.getNotesByPatient(1L);

        // 3. VÉRIFIER
        assertEquals(2, result.size());
        verify(noteRepository, times(1)).findByPatientId(1L);
    }

    // --- TEST 2 : ENREGISTRER UNE NOTE (AVEC SÉCURITÉ ID NULL) ---
    @Test
    public void testSaveNote_ShouldReturnNoteWithId() {
        // 1. PRÉPARER
        Note input = new Note();
        input.setContent("Patient en forme");

        Note saved = new Note();
        saved.setId("mongo-id-123"); // On simule l'ID généré par MongoDB
        saved.setContent("Patient en forme");

        when(noteRepository.save(any(Note.class))).thenReturn(saved);

        // 2. AGIR
        Note result = noteService.saveNote(input);

        // 3. VÉRIFIER
        assertNotNull(result.getId()); // On vérifie que MongoDB a bien créé un ID
        assertEquals("Patient en forme", result.getContent());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    // --- TEST 3 : SUPPRIMER UNE NOTE ---
    @Test
    public void testDeleteNote_ShouldCallRepository() {
        // 1. AGIR
        noteService.deleteNote("mongo-id-123");

        // 2. VÉRIFIER
        verify(noteRepository, times(1)).deleteById("mongo-id-123");
    }
}