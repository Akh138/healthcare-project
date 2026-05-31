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
 * Je vérifie que mes 3 routes répondent avec les bons codes pros (201, 204, etc.)
 */
@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc; // Mon simulateur de navigation

    @MockBean
    private AppointmentService appointmentService; // Je simule le service des RDV

    @Autowired
    private ObjectMapper objectMapper; // Pour transformer mes objets en JSON

    // --- TEST 1 : VÉRIFIER LA CRÉATION D'UN RDV ---
    @Test
    public void testCreateAppointment_ShouldReturnOk() throws Exception {
        // 1. PRÉPARER
        Appointment rdv = new Appointment(1L, 1L, "Katya", "ines", LocalDateTime.now(), "Checkup");
        when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(rdv);

        // 2. AGIR ET VÉRIFIER
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rdv)))
                // RÉPARATION : On attend maintenant 201 (isCreated) car c'est plus pro
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientName").value("Katya"));
    }

    // --- TEST 2 : VÉRIFIER LE PLANNING D'UN DOCTEUR ---
    @Test
    public void testGetAppointments_ShouldReturnList() throws Exception {
        when(appointmentService.getAppointmentsByDoctor("ines"))
                .thenReturn(Arrays.asList(new Appointment()));

        mockMvc.perform(get("/api/appointments/doctor/ines"))
                .andExpect(status().isOk()) // Ici le 200 est correct pour une simple liste
                .andExpect(jsonPath("$.length()").value(1));
    }

    // --- TEST 3 : VÉRIFIER L'ANNULATION D'UN RDV ---
    @Test
    public void testDeleteAppointment_ShouldReturnOk() throws Exception {
        // 1. AGIR ET VÉRIFIER
        mockMvc.perform(delete("/api/appointments/1"))
                // RÉPARATION : On attend 204 (isNoContent) car l'objet est supprimé
                .andExpect(status().isNoContent());
    }
}