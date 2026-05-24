package com.healthcare.ui.controller;

import com.healthcare.ui.model.MessageDTO;
import com.healthcare.ui.model.NoteDTO;
import com.healthcare.ui.model.PatientDTO;
import com.healthcare.ui.proxies.NoteProxy;
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

/**
 * CONTRÔLEUR PRINCIPAL DE L'INTERFACE
 */
@Controller
public class PatientController {

    @Autowired
    private PatientProxy patientProxy; // Microservice Patient (MySQL)

    @Autowired
    private NoteProxy noteProxy;       // Microservice Note (MongoDB) - Gère Notes ET Messages

    // =========================================================================
    // SECTION 1 : GESTION DES PATIENTS (MySQL)
    // =========================================================================

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

    // =========================================================================
    // SECTION 2 : DOSSIER MÉDICAL & MESSAGERIE (MongoDB NoSQL)
    // =========================================================================

    @GetMapping("/patients/notes/{id}")
    public String showPatientDossier(@PathVariable("id") Long id, Model model) {
        // 1. Identité (MySQL)
        PatientDTO patient = patientProxy.getPatientById(id);

        // 2. Notes (MongoDB)
        List<NoteDTO> notes = noteProxy.getNotesByPatient(id);

        // 3. On utilise noteProxy pour les messages aussi
        List<MessageDTO> messages = noteProxy.getMessagesByPatient(id);

        model.addAttribute("patient", patient);
        model.addAttribute("notes", notes);
        model.addAttribute("messages", messages);

        model.addAttribute("newNote", new NoteDTO());
        model.addAttribute("newMessage", new MessageDTO());

        return "patient-dossier";
    }

    @PostMapping("/patients/notes/add/{id}")
    public String addNoteToPatient(@PathVariable("id") Long id, @ModelAttribute("newNote") NoteDTO note, Authentication authentication) {
        note.setPatientId(id);
        note.setDoctorName("Dr. " + authentication.getName());
        noteProxy.createNote(note);
        return "redirect:/patients/notes/" + id;
    }

    @PostMapping("/patients/messages/add/{id}")
    public String sendMessageToPatient(@PathVariable("id") Long id, @ModelAttribute("newMessage") MessageDTO message, Authentication authentication) {
        message.setPatientId(id);
        message.setSenderName("Dr. " + authentication.getName());

        // CORRECTION : On utilise noteProxy
        noteProxy.sendMessage(message);

        return "redirect:/patients/notes/" + id;
    }

    // Méthode supprimer note
    @GetMapping("/patients/notes/delete/{noteId}/{patientId}")
    public String deleteNote(@PathVariable("noteId") String noteId, @PathVariable("patientId") Long patientId) {
        noteProxy.deleteNote(noteId); // Appelle le microservice NoSQL
        return "redirect:/patients/notes/" + patientId; // Revient sur le dossier
    }
}