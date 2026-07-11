package com.healthcare.patientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.patientservice.exception.ResourceNotFoundException; // J'importe mon erreur perso
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
 * On vérifie que les URLs renvoient les bons codes (200, 201, 404, 204).
 */
@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired //  @Autowired nest plus deprecié il vaut mieux utilisé : @AllArgsConstructor @RequiredArgConstructor
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllPatients_ShouldReturnList() throws Exception {
        when(patientService.getAllPatients()).thenReturn(Arrays.asList(new Patient(), new Patient()));
        mockMvc.perform(get("/api/patients")).andExpect(status().isOk());
    }

    @Test
    public void testGetPatientsByDoctor_ShouldReturnList() throws Exception {
        when(patientService.getPatientsByDoctor("ines")).thenReturn(Arrays.asList(new Patient()));
        mockMvc.perform(get("/api/patients/doctor/ines")).andExpect(status().isOk());
    }

    @Test
    public void testGetPatientById_Success() throws Exception {
        Patient p = new Patient(); p.setId(1L);
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(p));
        mockMvc.perform(get("/api/patients/1")).andExpect(status().isOk());
    }

    @Test
    public void testGetPatientById_NotFound() throws Exception {
        when(patientService.getPatientById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/patients/99")).andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePatient_ShouldReturn201() throws Exception {
        Patient p = Patient.builder().firstName("Katya").lastName("A").birthday(LocalDate.of(2000,1,1)).gender("F").address("P").phone("0102030405").email("t@t.com").build();
        when(patientService.createPatient(any(Patient.class))).thenReturn(p);
        mockMvc.perform(post("/api/patients").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(p))).andExpect(status().isCreated());
    }

    @Test
    public void testUpdatePatient_Success() throws Exception {
        Patient p = Patient.builder().firstName("Katya").lastName("A").birthday(LocalDate.of(2000,1,1)).gender("F").address("P").phone("0102030405").email("t@t.com").build();
        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(p);
        mockMvc.perform(put("/api/patients/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(p))).andExpect(status().isOk());
    }

    // --- RÉPARATION DU TEST QUI FAISAIT LE 500 ---
    @Test
    public void testUpdatePatient_NotFound_ShouldReturn404() throws Exception {
        // ICI JE LANCE MON ERREUR PERSO (ResourceNotFoundException)
        // C'est ça qui va forcer le 404 au lieu du 500 !
        when(patientService.updatePatient(eq(99L), any(Patient.class)))
                .thenThrow(new ResourceNotFoundException("Patient inconnu"));

        Patient p = Patient.builder().firstName("T").lastName("P").birthday(LocalDate.of(2000,1,1)).gender("M").address("A").phone("0102030405").email("t@t.com").build();

        mockMvc.perform(put("/api/patients/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isNotFound()); // Maintenant on aura bien 404
    }

    @Test
    public void testDeletePatient_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/patients/1")).andExpect(status().isNoContent());
    }

    @Test
    public void testGetPatientByAccount_Success() throws Exception {
        when(patientService.getPatientByAccount("katya")).thenReturn(Optional.of(new Patient()));
        mockMvc.perform(get("/api/patients/account/katya")).andExpect(status().isOk());
    }

    @Test
    public void testGetPatientByAccount_NotFound() throws Exception {
        when(patientService.getPatientByAccount("inconnu")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/patients/account/inconnu")).andExpect(status().isNotFound());
    }
}