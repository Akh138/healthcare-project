package com.healthcare.noteservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.noteservice.model.Note;
import com.healthcare.noteservice.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ICI JE TESTE LES URLS POUR LES NOTES MÉDICALES (MongoDB).
 * On vérifie que les 3 routes (Ajouter, Lister, Supprimer) répondent bien au format JSON.
 */
@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc; // Mon simulateur d'appels API

    @MockBean
    private NoteService noteService; // Je simule le service NoSQL

    @Autowired
    private ObjectMapper objectMapper; // Pour transformer mes objets Java en texte JSON

    // --- TEST 1 : VÉRIFIER LA CRÉATION D'UNE NOTE (POST) ---
    @Test
    public void testCreateNote_ShouldReturnOk() throws Exception {
        // 1. PRÉPARER
        Note note = new Note("id-123", 1L, "Dr. Habib", "Patient stable", null);
        when(noteService.saveNote(any(Note.class))).thenReturn(note);

        // 2. AGIR ET VÉRIFIER
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note))) // J'envoie la note en JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Patient stable"));
    }

    // --- TEST 2 : VÉRIFIER QU'ON PEUT VOIR TOUTES LES NOTES D'UN PATIENT ---
    @Test
    public void testGetNotesByPatient_ShouldReturnList() throws Exception {
        // 1. PRÉPARER
        when(noteService.getNotesByPatient(1L)).thenReturn(Arrays.asList(new Note(), new Note()));

        // 2. AGIR ET VÉRIFIER
        mockMvc.perform(get("/api/notes/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // --- TEST 3 : VÉRIFIER LA SUPPRESSION D'UNE NOTE ---
    @Test
    public void testDeleteNote_ShouldReturnOk() throws Exception {
        // 1. AGIR ET VÉRIFIER : On appelle la route DELETE
        mockMvc.perform(delete("/api/notes/id-123"))
                .andExpect(status().isOk());
    }
}