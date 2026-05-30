package com.healthcare.patientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.patientservice.model.Patient;
import com.healthcare.patientservice.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ICI JE TESTE TOUTES LES ROUTES DE MON MICROSERVICE PATIENT.
 * Le but est d'avoir 100% de couverture donc je teste les cas qui marchent
 * et aussi les cas d'erreurs (404, catch...).
 */
@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc; // Mon simulateur de navigateur pour appeler l'API

    @MockBean
    private PatientService patientService; // Je simule le service (Mock)

    @Autowired
    private ObjectMapper objectMapper; // Pour transformer mes objets en JSON

    // --- TEST 1 : VÉRIFIER QUE LA LISTE GLOBALE RÉPOND 200 OK ---
    @Test
    public void testGetAllPatients_ShouldReturnList() throws Exception {
        when(patientService.getAllPatients()).thenReturn(Arrays.asList(new Patient(), new Patient()));
        mockMvc.perform(get("/api/patients")).andExpect(status().isOk());
    }

    // --- TEST 2 : VÉRIFIER LA RECHERCHE PAR MÉDECIN ---
    @Test
    public void testGetPatientsByDoctor_ShouldReturnList() throws Exception {
        when(patientService.getPatientsByDoctor("ines")).thenReturn(Arrays.asList(new Patient()));
        mockMvc.perform(get("/api/patients/doctor/ines")).andExpect(status().isOk());
    }

    // --- TEST 3 : RECHERCHE PAR ID (SUCCÈS) ---
    @Test
    public void testGetPatientById_Success() throws Exception {
        Patient p = new Patient(); p.setId(1L);
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(p));
        mockMvc.perform(get("/api/patients/1")).andExpect(status().isOk());
    }

    // --- TEST 4 : RECHERCHE PAR ID (QUAND ON NE TROUVE RIEN -> 404) ---
    @Test
    public void testGetPatientById_NotFound() throws Exception {
        when(patientService.getPatientById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/patients/99")).andExpect(status().isNotFound());
    }

    // --- TEST 5 : CRÉATION D'UN PATIENT (CODE 201) ---
    @Test
    public void testCreatePatient_ShouldReturn201() throws Exception {
        // Je mets des données valides pour passer les @Valid du contrôleur
        Patient p = Patient.builder().firstName("Katya").lastName("A").birthday(LocalDate.of(2000,1,1)).gender("F").address("Paris").phone("0102030405").email("t@t.com").build();
        when(patientService.createPatient(any(Patient.class))).thenReturn(p);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated());
    }

    // --- TEST 6 : MISE À JOUR (SUCCÈS) ---
    @Test
    public void testUpdatePatient_Success() throws Exception {
        Patient p = Patient.builder().firstName("Katya").lastName("A").birthday(LocalDate.of(2000,1,1)).gender("F").address("Paris").phone("0102030405").email("t@t.com").build();
        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(p);

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk());
    }

    // --- TEST 7 : MISE À JOUR SI ERREUR (POUR PASSER DANS LE CATCH ET AVOIR LES 100%) ---
    @Test
    public void testUpdatePatient_NotFound_ShouldReturn404() throws Exception {
        // Je simule une erreur du service pour forcer le contrôleur à aller dans le catch
        when(patientService.updatePatient(eq(99L), any(Patient.class))).thenThrow(new RuntimeException("Inconnu"));

        Patient p = Patient.builder().firstName("T").lastName("P").birthday(LocalDate.of(2000,1,1)).gender("M").address("A").phone("0102030405").email("t@t.com").build();

        mockMvc.perform(put("/api/patients/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isNotFound());
    }

    // --- TEST 8 : SUPPRESSION (CODE 204) ---
    @Test
    public void testDeletePatient() throws Exception {
        mockMvc.perform(delete("/api/patients/1")).andExpect(status().isNoContent());
    }

    // --- TEST 9 : RECHERCHE PAR NOM DE COMPTE (QUAND ÇA MARCHE) ---
    @Test
    public void testGetPatientByAccount_Success() throws Exception {
        when(patientService.getPatientByAccount("katya")).thenReturn(Optional.of(new Patient()));
        mockMvc.perform(get("/api/patients/account/katya")).andExpect(status().isOk());
    }

    // --- TEST 10 : RECHERCHE PAR NOM DE COMPTE (QUAND ÇA RATE -> 404) ---
    @Test
    public void testGetPatientByAccount_NotFound() throws Exception {
        when(patientService.getPatientByAccount("inconnu")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/patients/account/inconnu")).andExpect(status().isNotFound());
    }
}