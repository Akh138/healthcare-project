package com.healthcare.patientservice.service;

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
 * Ici, on vérifie que la gestion du planning fonctionne bien avec les bons noms de méthodes.
 */
@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository; // Mon simulateur de base de données

    @InjectMocks
    private AppointmentService appointmentService; // Mon service où on teste la logique

    // --- TEST 1 : RÉCUPÉRER LE PLANNING D'UN MÉDECIN ---
    @Test
    public void testGetAppointmentsByDoctor_ShouldReturnList() {
        // 1. PRÉPARER
        String doctor = "ines";

        // ATTENTION : J'utilise ici le nom EXACT de la méthode de ton Repository
        when(appointmentRepository.findByDoctorUsernameOrderByDateTimeAsc(doctor))
                .thenReturn(Arrays.asList(new Appointment(), new Appointment()));

        // 2. AGIR
        List<Appointment> result = appointmentService.getAppointmentsByDoctor(doctor);

        // 3. VÉRIFIER
        assertEquals(2, result.size());
        // On vérifie que le service a bien appelé la méthode avec le bon nom
        verify(appointmentRepository, times(1)).findByDoctorUsernameOrderByDateTimeAsc(doctor);
    }

    // --- TEST 2 : CRÉER UN NOUVEAU RENDEZ-VOUS ---
    @Test
    public void testCreateAppointment_ShouldReturnSavedAppointment() {
        // 1. PRÉPARER
        Appointment input = new Appointment(null, 1L, "Katya", "ines", LocalDateTime.now(), "Contrôle");
        Appointment saved = new Appointment(10L, 1L, "Katya", "ines", LocalDateTime.now(), "Contrôle");

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(saved);

        // 2. AGIR
        Appointment result = appointmentService.createAppointment(input);

        // 3. VÉRIFIER
        assertNotNull(result.getId());
        assertEquals("Katya", result.getPatientName());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    // --- TEST 3 : ANNULER (SUPPRIMER) UN RENDEZ-VOUS ---
    @Test
    public void testDeleteAppointment_ShouldCallRepository() {
        // 1. AGIR : On demande de supprimer le rendez-vous n°10
        appointmentService.deleteAppointment(10L);

        // 2. VÉRIFIER
        verify(appointmentRepository, times(1)).deleteById(10L);
    }
}