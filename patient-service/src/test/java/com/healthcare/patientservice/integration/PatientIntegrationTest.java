package com.healthcare.patientservice.integration;

import com.healthcare.patientservice.model.Patient;
import com.healthcare.patientservice.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TEST D'INTÉGRATION DU SERVICE PATIENT.
 * Ici on teste tout en même temps : le Controller, le Service et la base de données H2.
 * C'est le test "Vrai de Vrai".
 */
@SpringBootTest // On lance tout le microservice
@AutoConfigureMockMvc // Pour simuler les appels HTTP sur nos routes
@ActiveProfiles("test") // On utilise la base H2 (en mémoire)
@Transactional // On vide la base après le test pour que ce soit toujours propre
public class PatientIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Mon simulateur de navigateur

    @Autowired
    private PatientRepository patientRepository; // Pour écrire directement en base de test

    @Test
    public void testFullFlow_SaveAndFindPatient() throws Exception {
        // --- 1. PRÉPARER ---
        // J'enregistre manuellement un vrai patient dans ma base de test H2
        Patient p = Patient.builder()
                .firstName("Test").lastName("INTEGRATION").birthday(LocalDate.of(1990, 1, 1))
                .gender("M").address("Lille").phone("0102030405").email("test@integration.com")
                .build();
        p = patientRepository.save(p);

        // --- 2. AGIR ET VÉRIFIER ---
        // Je fais un vrai appel HTTP sur l'URL pour voir si le système le retrouve bien en base
        mockMvc.perform(get("/api/patients/" + p.getId()))
                .andExpect(status().isOk()) // On vérifie que le code est 200 OK
                .andExpect(jsonPath("$.firstName").value("Test")) // On vérifie que le prénom est correct
                .andExpect(jsonPath("$.lastName").value("INTEGRATION"));
    }
}