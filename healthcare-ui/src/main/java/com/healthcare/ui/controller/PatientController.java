package com.healthcare.ui.controller;

import com.healthcare.ui.model.PatientDTO;
import com.healthcare.ui.proxies.PatientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/patients")
    public String listPatients(Model model, Authentication authentication) {
        String currentDoctor = authentication.getName();
        List<PatientDTO> patients = patientProxy.getPatientsByDoctor(currentDoctor);
        model.addAttribute("patients", patients);
        return "patient-list";
    }

    @GetMapping("/patients/add")
    public String showAddForm(Model model) {
        model.addAttribute("patient", new PatientDTO());
        return "patient-add";
    }

    @PostMapping("/patients/add")
    public String savePatient(@ModelAttribute("patient") PatientDTO patient, Authentication authentication) {
        patient.setDoctorUsername(authentication.getName());
        patientProxy.createPatient(patient);
        return "redirect:/patients";
    }

    @GetMapping("/patients/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        PatientDTO patient = patientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient-update";
    }

    @PostMapping("/patients/update/{id}")
    public String updatePatient(@PathVariable("id") Long id, @ModelAttribute("patient") PatientDTO patient, Authentication authentication) {
        // TRÈS IMPORTANT : On force l'ID et le nom du médecin avant d'envoyer
        patient.setId(id);
        patient.setDoctorUsername(authentication.getName());

        patientProxy.updatePatient(id, patient);
        return "redirect:/patients";
    }

    @GetMapping("/patients/delete/{id}")
    public String deletePatient(@PathVariable("id") Long id) {
        patientProxy.deletePatient(id);
        return "redirect:/patients";
    }
}