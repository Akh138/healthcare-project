package com.healthcare.patientservice.service;

import com.healthcare.patientservice.exception.ResourceNotFoundException; // J'importe mon erreur perso
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
 * Ici, je vérifie que toute ma logique métier est correcte.
 * J'utilise Mockito pour simuler MySQL.
 */
@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository; // Mon simulateur de table MySQL

    @InjectMocks
    private PatientService patientService; // Mon service où on injecte le simulateur

    // --- TEST 1 : VÉRIFIER QU'ON RÉCUPÈRE BIEN TOUTE LA LISTE ---
    @Test
    public void testGetAllPatients_ShouldReturnList() {
        // Je simule une liste avec 2 patients
        when(patientRepository.findAll()).thenReturn(Arrays.asList(new Patient(), new Patient()));

        List<Patient> result = patientService.getAllPatients();

        assertEquals(2, result.size());
        verify(patientRepository, times(1)).findAll();
    }

    // --- TEST 2 : VÉRIFIER LA RECHERCHE PAR MÉDECIN ---
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
        Patient input = new Patient();
        Patient saved = new Patient();
        saved.setId(1L); // On simule l'ID généré par la base

        when(patientRepository.save(any(Patient.class))).thenReturn(saved);

        Patient result = patientService.createPatient(input);

        assertNotNull(result.getId());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    // --- TEST 5 : VÉRIFIER LA MISE À JOUR (UPDATE) ---
    @Test
    public void testUpdatePatient_ShouldReturnUpdatedPatient() {
        // Je prépare un patient qui existe déjà
        Patient existing = new Patient(); existing.setId(1L); existing.setLastName("OLD");
        // Je prépare les nouvelles infos
        Patient updatedInfos = new Patient(); updatedInfos.setLastName("NEW");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientRepository.save(any(Patient.class))).thenReturn(updatedInfos);

        Patient result = patientService.updatePatient(1L, updatedInfos);

        assertEquals("NEW", result.getLastName());
    }

    // --- TEST 6 : VÉRIFIER LA SUPPRESSION QUAND TOUT VA BIEN ---
    @Test
    public void testDeletePatient_ShouldCallRepository() {
        // IMPORTANT : Pour que ça marche, je dois dire au simulateur que le patient EXISTE.
        // Sinon, mon code s'arrête à cause du "if (!existsById) { throw ... }"
        when(patientRepository.existsById(1L)).thenReturn(true);

        // Maintenant j'appelle la suppression
        patientService.deletePatient(1L);

        // Je vérifie que la suppression a bien été demandée à la base
        verify(patientRepository, times(1)).deleteById(1L);
    }

    // --- TEST 7 : VÉRIFIER QUE ÇA LANCE BIEN UNE ERREUR SI LE PATIENT N'EXISTE PAS ---
    // Ce test est celui qui va te donner les 100% car il teste la ligne du "throw"
    @Test
    public void testDeletePatient_NotFound_ShouldThrowException() {
        // 1. Je simule le cas où le patient n'existe pas (ID 99)
        when(patientRepository.existsById(99L)).thenReturn(false);

        // 2. Je vérifie que le service lance bien mon erreur personnalisée
        assertThrows(ResourceNotFoundException.class, () -> {
            patientService.deletePatient(99L);
        });
    }

    // --- TEST 8 : VÉRIFIER LA RECHERCHE PAR NOM DE COMPTE ---
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