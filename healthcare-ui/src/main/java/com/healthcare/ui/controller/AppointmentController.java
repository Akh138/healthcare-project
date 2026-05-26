package com.healthcare.ui.controller;

import com.healthcare.ui.dto.AppointmentDTO;
import com.healthcare.ui.dto.PatientDTO;
import com.healthcare.ui.proxies.PatientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppointmentController {

    @Autowired
    private PatientProxy patientProxy; // On utilise le même proxy car les RDV sont dans le patient-service

    // 1. AFFICHER LE PLANNING DU MÉDECIN
    @GetMapping("/consultations")
    public String showPlanning(Model model, Authentication authentication) {
        String doctor = authentication.getName();

        // On récupère les rendez-vous
        List<AppointmentDTO> appointments = patientProxy.getAppointmentsByDoctor(doctor);

        // On récupère aussi la liste des patients (pour pouvoir en choisir un dans le formulaire)
        List<PatientDTO> patients = patientProxy.getPatientsByDoctor(doctor);

        model.addAttribute("appointments", appointments);
        model.addAttribute("patients", patients);
        model.addAttribute("newAppointment", new AppointmentDTO());

        return "planning"; // Cherchera planning.html
    }

    // 2. ENREGISTRER UN NOUVEAU RDV
    @PostMapping("/consultations/add")
    public String addAppointment(@ModelAttribute("newAppointment") AppointmentDTO appointment, Authentication authentication) {
        appointment.setDoctorUsername(authentication.getName());

        // On récupère le nom du patient pour l'affichage (optionnel mais plus simple)
        PatientDTO p = patientProxy.getPatientById(appointment.getPatientId());
        appointment.setPatientName(p.getLastName() + " " + p.getFirstName());

        patientProxy.createAppointment(appointment);
        return "redirect:/consultations";
    }

    // 3. ANNULER UN RDV
    @GetMapping("/consultations/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        patientProxy.deleteAppointment(id);
        return "redirect:/consultations";
    }
}