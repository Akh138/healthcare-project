package com.healthcare.patientservice.service;

import com.healthcare.patientservice.exception.ResourceNotFoundException; // J'importe mon erreur perso
import com.healthcare.patientservice.model.Appointment;
import com.healthcare.patientservice.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TEST UNITAIRE DU SERVICE DES RENDEZ-VOUS.
 * J'ai mis à jour les tests pour qu'ils passent ma nouvelle sécurité (existsById).
 */
@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository; // Mon simulateur de base de données

    @InjectMocks
    private AppointmentService appointmentService; // Mon service à tester

    // --- TEST 1 : VOIR LE PLANNING ---
    @Test
    public void testGetAppointmentsByDoctor_ShouldReturnList() {
        String doctor = "ines";
        when(appointmentRepository.findByDoctorUsernameOrderByDateTimeAsc(doctor))
                .thenReturn(Arrays.asList(new Appointment(), new Appointment()));

        List<Appointment> result = appointmentService.getAppointmentsByDoctor(doctor);

        assertEquals(2, result.size());
        verify(appointmentRepository, times(1)).findByDoctorUsernameOrderByDateTimeAsc(doctor);
    }

    // --- TEST 2 : CRÉER UN RDV ---
    @Test
    public void testCreateAppointment_ShouldReturnSavedAppointment() {
        Appointment input = new Appointment(null, 1L, "Katya", "ines", LocalDateTime.now(), "Contrôle");
        Appointment saved = new Appointment(10L, 1L, "Katya", "ines", LocalDateTime.now(), "Contrôle");

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(saved);

        Appointment result = appointmentService.createAppointment(input);

        assertNotNull(result.getId());
        assertEquals("Katya", result.getPatientName());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    // --- TEST 3 : ANNULER UN RDV QUAND IL EXISTE (CORRIGÉ) ---
    @Test
    public void testDeleteAppointment_ShouldCallRepository() {
        // 1. PRÉPARER : Je dis au simulateur que le RDV n°10 EXISTE bien
        // C'est ça qui manquait pour passer le "if" dans mon code !
        when(appointmentRepository.existsById(10L)).thenReturn(true);

        // 2. AGIR
        appointmentService.deleteAppointment(10L);

        // 3. VÉRIFIER
        verify(appointmentRepository, times(1)).deleteById(10L);
    }

    // --- TEST 4 : VÉRIFIER L'ERREUR SI LE RDV N'EXISTE PAS (POUR LE 100%) ---
    @Test
    public void testDeleteAppointment_NotFound_ShouldThrowException() {
        // 1. Je simule le cas où le RDV n'existe pas
        when(appointmentRepository.existsById(99L)).thenReturn(false);

        // 2. Je vérifie que ça lance bien mon erreur 404 perso
        assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.deleteAppointment(99L);
        });
    }
}