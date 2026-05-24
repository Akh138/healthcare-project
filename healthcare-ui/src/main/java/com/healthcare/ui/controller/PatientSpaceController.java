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
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PatientSpaceController {

    @Autowired
    private PatientProxy patientProxy;

    @Autowired
    private NoteProxy noteProxy;

    @GetMapping("/my-dossier")
    public String showMyDossier(Model model, Authentication authentication) {
        // 1. On utilise le nom d'utilisateur pour trouver le patient correspondant
        // NOTE : Ici on suppose que le username du compte = le lastName ou email du patient
        // Pour ton test, on va chercher par le nom "ines" (celui que tu viens de créer)
        String username = authentication.getName();

        // On récupère les infos du patient (MySQL)
        // Note : On utilise la même méthode que pour le médecin mais pour soi-même
        List<PatientDTO> patients = patientProxy.getPatientsByDoctor(username);

        // Si on ne trouve rien (car le patient n'est pas encore lié à un compte),
        // on peut afficher un message. Mais pour l'instant, on va prendre le premier trouvé.
        if (!patients.isEmpty()) {
            PatientDTO patient = patients.get(0);
            List<NoteDTO> notes = noteProxy.getNotesByPatient(patient.getId());
            List<MessageDTO> messages = noteProxy.getMessagesByPatient(patient.getId());

            model.addAttribute("patient", patient);
            model.addAttribute("notes", notes);
            model.addAttribute("messages", messages);
            model.addAttribute("newMessage", new MessageDTO());
        }

        return "patient/my-dossier"; // On va créer ce dossier et ce fichier
    }

    // Le patient peut aussi envoyer un message au médecin !
    @PostMapping("/my-dossier/message")
    public String sendMessage(@ModelAttribute("newMessage") MessageDTO message, Authentication authentication) {
        // On récupère l'ID du patient (ines) pour savoir à quel dossier l'attacher
        List<PatientDTO> patients = patientProxy.getPatientsByDoctor(authentication.getName());
        if (!patients.isEmpty()) {
            message.setPatientId(patients.get(0).getId());
            message.setSenderName(authentication.getName()); // "ines"
            noteProxy.sendMessage(message);
        }
        return "redirect:/my-dossier";
    }
}