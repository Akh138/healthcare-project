package com.healthcare.patientservice.service;

import com.healthcare.patientservice.exception.ResourceNotFoundException; // J'importe mon erreur perso
import com.healthcare.patientservice.model.Appointment;
import com.healthcare.patientservice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // J'enregistre un nouveau rendez-vous
    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    // Je récupère le planning complet d'un médecin
    public List<Appointment> getAppointmentsByDoctor(String doctorUsername) {
        return appointmentRepository.findByDoctorUsernameOrderByDateTimeAsc(doctorUsername);
    }

    // JE SUPPRIME (ANNULE) UN RENDEZ-VOUS
    public void deleteAppointment(Long id) {
        // JE SÉCURISE LA SUPPRESSION : Si le RDV n'existe pas, je lance une erreur 404
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rendez-vous introuvable (ID: " + id + ")");
        }
        appointmentRepository.deleteById(id);
    }
}