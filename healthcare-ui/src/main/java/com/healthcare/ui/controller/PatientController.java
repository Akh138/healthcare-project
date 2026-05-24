package com.healthcare.ui.controller;

import com.healthcare.ui.model.PatientDTO;
import com.healthcare.ui.proxies.PatientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Import pour la sécurité
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private PatientProxy patientProxy;

    // 1. LISTER LES PATIENTS (Seulement ceux du médecin connecté)
    @GetMapping("/patients")
    public String listPatients(Model model, Authentication authentication) {
        // On récupère le nom du médecin connecté grâce à "authentication".
        String currentDoctor = authentication.getName();

        // On demande au Proxy uniquement les patients de ce médecin
        List<PatientDTO> patients = patientProxy.getPatientsByDoctor(currentDoctor);

        model.addAttribute("patients", patients);
        return "patient-list";
    }

    // 2. AFFICHER FORMULAIRE AJOUT
    @GetMapping("/patients/add")
    public String showAddForm(Model model) {
        model.addAttribute("patient", new PatientDTO());
        return "patient-add";
    }

    // 3. ENREGISTRER PATIENT
    @PostMapping("/patients/add")
    public String savePatient(@ModelAttribute("patient") PatientDTO patient, Authentication authentication) {
        // On remplit automatiquement le nom du médecin avant l'envoi
        patient.setDoctorUsername(authentication.getName());

        patientProxy.createPatient(patient);
        return "redirect:/patients";
    }

    // 4. SUPPRIMER UN PATIENT
    @GetMapping("/patients/delete/{id}")
    public String deletePatient(@PathVariable("id") Long id) {
        patientProxy.deletePatient(id);
        return "redirect:/patients";
    }
}