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

    // On affiche la liste des patients REELS (ceux déjà validés)
    @GetMapping("/patients")
    public String listPatients(Model model, Authentication authentication) {
        String currentDoctor = authentication.getName();
        // On récupère tout et on filtre pour ne pas voir les "Demandes" ici
        List<PatientDTO> patients = patientProxy.getPatientsByDoctor(currentDoctor)
                .stream()
                .filter(p -> !"NOUVEAU DOSSIER".equals(p.getLastName()))
                .toList();

        model.addAttribute("patients", patients);
        return "patient/patient-list";
    }

    // NOUVEAU : On affiche seulement les nouveaux inscrits en attente de validation
    @GetMapping("/patients/requests")
    public String listRequests(Model model, Authentication authentication) {
        String currentDoctor = authentication.getName();
        // On filtre pour ne garder que ceux qui ont le nom par défaut "NOUVEAU DOSSIER"
        List<PatientDTO> requests = patientProxy.getPatientsByDoctor(currentDoctor)
                .stream()
                .filter(p -> "NOUVEAU DOSSIER".equals(p.getLastName()))
                .toList();

        model.addAttribute("requests", requests);
        return "patient/requests"; // On renvoie vers le nouveau fichier requests.html
    }

    // NOUVEAU : Quand on clique sur "Valider", on redirige vers le formulaire d'édition
    // Comme ça le médecin peut remplir les vraies infos (Vrai Nom, Prénom, etc.)
    @GetMapping("/patients/validate/{id}")
    public String validatePatient(@PathVariable("id") Long id) {
        return "redirect:/patients/edit/" + id;
    }

    // On affiche le formulaire pour ajouter un nouveau patient manuellement
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

        // Si on vient de valider un nouveau dossier, on retourne à la liste globale
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
        PatientDTO patient = patientProxy.getPatientById(id);
        List<NoteDTO> notes = noteProxy.getNotesByPatient(id);
        List<MessageDTO> messages = noteProxy.getMessagesByPatient(id);

        model.addAttribute("patient", patient);
        model.addAttribute("notes", notes);
        model.addAttribute("messages", messages);

        model.addAttribute("newNote", new NoteDTO());
        model.addAttribute("newMessage", new MessageDTO());

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
        noteProxy.sendMessage(message);
        return "redirect:/patients/notes/" + id;
    }

    // Méthode pour supprimer une note spécifique dans MongoDB
    @GetMapping("/patients/notes/delete/{noteId}/{patientId}")
    public String deleteNote(@PathVariable("noteId") String noteId, @PathVariable("patientId") Long patientId) {
        noteProxy.deleteNote(noteId);
        return "redirect:/patients/notes/" + patientId;
    }
}