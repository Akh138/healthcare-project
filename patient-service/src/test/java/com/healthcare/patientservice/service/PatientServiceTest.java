package com.healthcare.patientservice.service;

import com.healthcare.patientservice.model.Patient;
import com.healthcare.patientservice.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TEST UNITAIRE DU SERVICE PATIENT.
 * Ici, je vérifie que mon code Java manipule bien les données des patients.
 * J'utilise Mockito pour simuler la base de données MySQL.
 */
@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository; // Mon simulateur de base de données

    @InjectMocks
    private PatientService patientService; // Mon vrai service où on injecte le simulateur

    // --- TEST 1 : VÉRIFIER QU'ON RÉCUPÈRE TOUS LES PATIENTS (POUR L'ADMIN) ---
    @Test
    public void testGetAllPatients_ShouldReturnList() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(new Patient(), new Patient()));

        List<Patient> result = patientService.getAllPatients();

        assertEquals(2, result.size());
        verify(patientRepository, times(1)).findAll();
    }

    // --- TEST 2 : VÉRIFIER LA RECHERCHE PAR DOCTEUR (TRÈS IMPORTANT) ---
    @Test
    public void testGetPatientsByDoctor_ShouldReturnFilteredList() {
        String docName = "ines";
        when(patientRepository.findByDoctorUsername(docName)).thenReturn(Arrays.asList(new Patient()));

        List<Patient> result = patientService.getPatientsByDoctor(docName);

        assertEquals(1, result.size());
        verify(patientRepository, times(1)).findByDoctorUsername(docName);
    }

    // --- TEST 3 : VÉRIFIER QU'ON TROUVE UN PATIENT PAR SON ID ---
    @Test
    public void testGetPatientById_ShouldReturnPatient() {
        Patient p = new Patient();
        p.setId(1L);
        p.setFirstName("Katya");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(p));

        Optional<Patient> result = patientService.getPatientById(1L);

        assertTrue(result.isPresent());
        assertEquals("Katya", result.get().getFirstName());
    }

    // --- TEST 4 : VÉRIFIER LA CRÉATION D'UN NOUVEAU PATIENT ---
    @Test
    public void testCreatePatient_ShouldReturnSavedPatient() {
        Patient input = new Patient(null, "Katya", "AKERIM", LocalDate.now(), "F", "Paris", "0600", "mail@mail.com", "ines", "katya", null);
        Patient saved = input;
        saved.setId(1L);

        when(patientRepository.save(any(Patient.class))).thenReturn(saved);

        Patient result = patientService.createPatient(input);

        assertNotNull(result.getId());
        assertEquals("Katya", result.getFirstName());
    }

    // --- TEST 5 : VÉRIFIER LA MISE À JOUR (UPDATE) ---
    @Test
    public void testUpdatePatient_ShouldReturnUpdatedPatient() {
        Patient existing = new Patient(1L, "Katya", "OLD", LocalDate.now(), "F", "Paris", "0600", "mail@mail.com", "ines", "katya", null);
        Patient updatedInfos = new Patient(1L, "Katya", "NEW", LocalDate.now(), "F", "Paris", "0600", "mail@mail.com", "ines", "katya", null);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientRepository.save(any(Patient.class))).thenReturn(updatedInfos);

        Patient result = patientService.updatePatient(1L, updatedInfos);

        assertEquals("NEW", result.getLastName());
    }

    // --- TEST 6 : VÉRIFIER LA SUPPRESSION ---
    @Test
    public void testDeletePatient_ShouldCallRepository() {
        patientService.deletePatient(1L);
        verify(patientRepository, times(1)).deleteById(1L);
    }

    // --- TEST 7 : VÉRIFIER LA RECHERCHE PAR NOM DE COMPTE (POUR LE LOGIN PATIENT) ---
    @Test
    public void testGetPatientByAccount_ShouldReturnPatient() {
        Patient p = new Patient();
        p.setPatientUsername("katya");

        when(patientRepository.findByPatientUsername("katya")).thenReturn(Optional.of(p));

        Optional<Patient> result = patientService.getPatientByAccount("katya");

        assertTrue(result.isPresent());
        assertEquals("katya", result.get().getPatientUsername());
    }
}