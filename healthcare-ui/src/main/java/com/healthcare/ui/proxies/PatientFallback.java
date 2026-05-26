package com.healthcare.ui.proxies;

import com.healthcare.ui.dto.AppointmentDTO;
import com.healthcare.ui.dto.PatientDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PatientFallback implements PatientProxy {

    @Override
    public List<PatientDTO> getAllPatients() {
        // En cas de panne du microservice, on renvoie NULL.
        // C'est notre signal pour le HTML.
        System.out.println("LOG [Fallback]: Le microservice Patient est hors ligne.");
        return null;
    }

    @Override
    public List<PatientDTO> getPatientsByDoctor(String username) {
        System.out.println("LOG [Fallback]: Impossible de charger les patients du docteur " + username);
        return null;
    }

    @Override
    public PatientDTO getPatientById(Long id) {
        return null;
    }

    @Override
    public PatientDTO createPatient(PatientDTO patient) {
        return null;
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patient) {
        return null;
    }

    @Override
    public void deletePatient(Long id) {
        // On ne fait rien car le service est injoignable
    }

    @Override
    public PatientDTO getPatientByAccount(String username) {
        return null;
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByDoctor(String username) {
        return new ArrayList<>();
    }

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointment) {
        return null;
    }

    @Override
    public void deleteAppointment(Long id) { }
}