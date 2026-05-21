package com.healthcare.ui.controller;

import com.healthcare.ui.model.PatientDTO;
import com.healthcare.ui.proxies.PatientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private PatientProxy patientProxy;

    // 1. Afficher la liste des patients
    @GetMapping("/patients")
    public String listPatients(Model model) {
        List<PatientDTO> patients = patientProxy.getAllPatients();
        model.addAttribute("patients", patients);
        return "patient-list"; // Cherchera patient-list.html
    }

    // 2. Afficher le formulaire d'ajout
    @GetMapping("/patients/add")
    public String showAddForm(Model model) {
        model.addAttribute("patient", new PatientDTO());
        return "patient-add"; // Cherchera patient-add.html
    }

    // 3. Enregistrer un nouveau patient
    @PostMapping("/patients/add")
    public String savePatient(@ModelAttribute("patient") PatientDTO patient) {
        patientProxy.createPatient(patient);
        return "redirect:/patients"; // Retourne à la liste après l'ajout
    }
}