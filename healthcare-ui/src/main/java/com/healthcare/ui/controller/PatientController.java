package com.healthcare.ui.controller;

import com.healthcare.ui.dto.MessageDTO;
import com.healthcare.ui.dto.NoteDTO;
import com.healthcare.ui.dto.PatientDTO;
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
 * C'EST LE CONTRÔLEUR QUI GÈRE TOUTE L'INTERFACE DES PATIENTS (LISTE, AJOUT, NOTES...)
 */
@Controller
public class PatientController {

    @Autowired
    private PatientProxy patientProxy; // Pour discuter avec le microservice Patient (les données MySQL)

    @Autowired
    private NoteProxy noteProxy;       // Pour discuter avec le microservice Note (les données NoSQL de MongoDB)

    // =========================================================================
    // SECTION 1 : ICI ON GÈRE LES INFOS DE BASE DU PATIENT (DANS MySQL)
    // =========================================================================

    // On affiche la liste des patients du médecin qui est connecté
    @GetMapping("/patients")
    public String listPatients(Model model, Authentication authentication) {
        String currentDoctor = authentication.getName();
        List<PatientDTO> patients = patientProxy.getPatientsByDoctor(currentDoctor);
        model.addAttribute("patients", patients);

        // On retourne la vue dans le dossier patient
        return "patient/patient-list";
    }

    // On affiche le formulaire pour ajouter un nouveau patient
    @GetMapping("/patients/add")
    public String showAddForm(Model model) {
        model.addAttribute("patient", new PatientDTO());
        return "patient/patient-add";
    }

    // On enregistre le nouveau patient et on le lie au médecin actuel
    @PostMapping("/patients/add")
    public String savePatient(@ModelAttribute("patient") PatientDTO patient, Authentication authentication) {
        patient.setDoctorUsername(authentication.getName());
        patientProxy.createPatient(patient);
        return "redirect:/patients";
    }

    // On va chercher les infos d'un patient pour les modifier
    @GetMapping("/patients/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        PatientDTO patient = patientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient/patient-update";
    }

    // On enregistre les modifications faites sur le patient
    @PostMapping("/patients/update/{id}")
    public String updatePatient(@PathVariable("id") Long id, @ModelAttribute("patient") PatientDTO patient, Authentication authentication) {
        patient.setId(id);
        patient.setDoctorUsername(authentication.getName());
        patientProxy.updatePatient(id, patient);
        return "redirect:/patients";
    }

    // Pour supprimer un patient de la liste
    @GetMapping("/patients/delete/{id}")
    public String deletePatient(@PathVariable("id") Long id) {
        patientProxy.deletePatient(id);
        return "redirect:/patients";
    }

    // =========================================================================
    // SECTION 2 : ICI ON GÈRE LE DOSSIER MÉDICAL ET LES NOTES (DANS MongoDB)
    // =========================================================================

    // C'est ici qu'on affiche tout le dossier d'un patient (Infos + Notes + Messages)
    @GetMapping("/patients/notes/{id}")
    public String showPatientDossier(@PathVariable("id") Long id, Model model) {
        // 1. On récupère les infos d'identité (via MySQL)
        PatientDTO patient = patientProxy.getPatientById(id);

        // 2. On récupère les notes médicales (via MongoDB)
        List<NoteDTO> notes = noteProxy.getNotesByPatient(id);

        // 3. On récupère aussi les messages de la messagerie (via MongoDB)
        List<MessageDTO> messages = noteProxy.getMessagesByPatient(id);

        model.addAttribute("patient", patient);
        model.addAttribute("notes", notes);
        model.addAttribute("messages", messages);

        // On prépare des objets vides pour les formulaires d'ajout de note/message
        model.addAttribute("newNote", new NoteDTO());
        model.addAttribute("newMessage", new MessageDTO());

        // On pointe vers le fichier dans le dossier patient
        return "patient/patient-dossier";
    }

    // Pour ajouter une nouvelle note médicale dans le dossier MongoDB
    @PostMapping("/patients/notes/add/{id}")
    public String addNoteToPatient(@PathVariable("id") Long id, @ModelAttribute("newNote") NoteDTO note, Authentication authentication) {
        note.setPatientId(id);
        note.setDoctorName("Dr. " + authentication.getName());
        noteProxy.createNote(note);
        return "redirect:/patients/notes/" + id;
    }

    // Pour envoyer un message au patient via la messagerie NoSQL
    @PostMapping("/patients/messages/add/{id}")
    public String sendMessageToPatient(@PathVariable("id") Long id, @ModelAttribute("newMessage") MessageDTO message, Authentication authentication) {
        message.setPatientId(id);
        message.setSenderName("Dr. " + authentication.getName());

        // On passe par le proxy des notes car elles partagent le même microservice MongoDB
        noteProxy.sendMessage(message);

        return "redirect:/patients/notes/" + id;
    }

    // Méthode pour supprimer une note spécifique dans MongoDB
    @GetMapping("/patients/notes/delete/{noteId}/{patientId}")
    public String deleteNote(@PathVariable("noteId") String noteId, @PathVariable("patientId") Long patientId) {
        noteProxy.deleteNote(noteId); // Supprime le document dans la collection NoSQL
        return "redirect:/patients/notes/" + patientId; // On recharge la page du dossier
    }
}