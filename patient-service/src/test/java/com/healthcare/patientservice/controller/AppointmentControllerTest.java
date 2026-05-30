package com.healthcare.patientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.patientservice.model.Appointment;
import com.healthcare.patientservice.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ICI JE TESTE LES URLS DU PLANNING (RENDEZ-VOUS).
 * Je vérifie que mes 3 routes (Ajouter, Voir, Supprimer) répondent bien.
 */
@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc; // Mon simulateur de navigateur pour appeler l'API

    @MockBean
    private AppointmentService appointmentService; // Je simule le service des RDV

    @Autowired
    private ObjectMapper objectMapper; // Outil pour transformer mes objets en JSON

    // --- TEST 1 : VÉRIFIER QUE LA CRÉATION D'UN RDV FONCTIONNE ---
    @Test
    public void testCreateAppointment_ShouldReturnOk() throws Exception {
        // 1. PRÉPARER : Je crée un rendez-vous de test
        Appointment rdv = new Appointment(1L, 1L, "Katya", "ines", LocalDateTime.now(), "Checkup");

        // Je simule : "Quand on appelle createAppointment, renvoie mon rdv"
        when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(rdv);

        // 2. AGIR ET VÉRIFIER
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rdv))) // J'envoie le RDV en format JSON
                .andExpect(status().isOk()) // On vérifie que ça répond 200 OK
                .andExpect(jsonPath("$.patientName").value("Katya"));
    }

    // --- TEST 2 : VÉRIFIER QU'ON PEUT VOIR LE PLANNING D'UN DOCTEUR ---
    @Test
    public void testGetAppointments_ShouldReturnList() throws Exception {
        // 1. PRÉPARER
        when(appointmentService.getAppointmentsByDoctor("ines"))
                .thenReturn(Arrays.asList(new Appointment()));

        // 2. AGIR ET VÉRIFIER
        mockMvc.perform(get("/api/appointments/doctor/ines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // --- TEST 3 : VÉRIFIER QU'ON PEUT ANNULER UN RDV ---
    @Test
    public void testDeleteAppointment_ShouldReturnOk() throws Exception {
        // 1. AGIR ET VÉRIFIER : On appelle l'URL de suppression pour l'ID 1
        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isOk()); // Le contrôleur renvoie 200 quand c'est fini
    }
}