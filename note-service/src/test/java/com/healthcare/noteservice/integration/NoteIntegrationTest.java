package com.healthcare.noteservice.integration;

import com.healthcare.noteservice.model.Note;
import com.healthcare.noteservice.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TEST D'INTÉGRATION DU SERVICE NOTES (MongoDB).
 * Ici, on teste toute la chaîne avec un vrai MongoDB simulé en mémoire.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // On active le profil 'test' pour utiliser Embedded MongoDB
public class NoteIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Mon simulateur de requêtes

    @Autowired
    private NoteRepository noteRepository; // Pour enregistrer des notes en base de test

    @Test
    public void testFullFlow_SaveNoteAndFindIt() throws Exception {
        // --- 1. PRÉPARER ---
        // J'efface tout au début pour être sûr
        noteRepository.deleteAll();

        // J'enregistre une vraie note dans mon MongoDB de test
        Note n = new Note(null, 2L, "Dr. Habib", "Analyse de sang OK", null);
        n = noteRepository.save(n);

        // --- 2. AGIR ET VÉRIFIER ---
        // Je fais un appel HTTP pour voir si l'API retrouve bien ma note
        mockMvc.perform(get("/api/notes/patient/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].content").value("Analyse de sang OK"))
                .andExpect(jsonPath("$.[0].doctorName").value("Dr. Habib"));
    }
}