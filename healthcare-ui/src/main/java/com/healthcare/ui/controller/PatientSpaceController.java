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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// C'est ici qu'on gère l'espace privé du patient quand il est connecté
@Controller
public class PatientSpaceController {

    @Autowired
    private PatientProxy patientProxy; // Pour retrouver sa fiche d'identité

    @Autowired
    private NoteProxy noteProxy; // Pour ses notes et sa messagerie NoSQL

    // J'AFFICHE MON PROPRE DOSSIER (CÔTÉ PATIENT)
    @GetMapping("/my-dossier")
    public String showMyDossier(Model model, Authentication authentication) {
        // Je récupère le pseudo de la personne connectée (ex: "katya")
        String currentAccount = authentication.getName();

        // 1. Je cherche ma fiche médicale qui correspond à mon pseudo
        PatientDTO myProfile = patientProxy.getPatientByAccount(currentAccount);

        // 2. AMÉLIORATION : Je vérifie si mon inscription est validée
        // Si je n'ai pas de fiche OU si mon nom est encore "NOUVEAU DOSSIER"
        // Ça veut dire que le médecin n'a pas encore validé ma demande.
        if (myProfile == null || "NOUVEAU DOSSIER".equals(myProfile.getLastName())) {
            // Dans ce cas, j'affiche la page qui dit "Dossier en attente"
            return "patient/no-dossier";
        }

        // 3. Si mon nom a été changé (validation faite), je peux voir mes notes et messages
        List<NoteDTO> notes = noteProxy.getNotesByPatient(myProfile.getId());
        List<MessageDTO> messages = noteProxy.getMessagesByPatient(myProfile.getId());

        model.addAttribute("patient", myProfile);
        model.addAttribute("notes", notes);
        model.addAttribute("messages", messages);

        // Je prépare un objet vide pour si je veux envoyer un message à mon médecin
        model.addAttribute("newMessage", new MessageDTO());

        // J'affiche enfin mon vrai dossier médical !
        return "patient/my-dossier";
    }

    // POUR ENVOYER UN MESSAGE À MON MÉDECIN
    @PostMapping("/my-dossier/message")
    public String sendMessageToDoctor(@ModelAttribute("newMessage") MessageDTO message, Authentication authentication) {
        String currentAccount = authentication.getName();
        PatientDTO myProfile = patientProxy.getPatientByAccount(currentAccount);

        if (myProfile != null) {
            message.setPatientId(myProfile.getId());
            // Je signe le message avec mon pseudo (ex: "katya")
            message.setSenderName(currentAccount);
            // J'envoie ça au microservice de messagerie (NoSQL)
            noteProxy.sendMessage(message);
        }

        return "redirect:/my-dossier";
    }

    // PETIT PLUS : POUR METTRE À JOUR MA PHOTO DE PROFIL
    @PostMapping("/my-dossier/upload-photo")
    public String uploadPhoto(@RequestParam("photo") String photoBase64, Authentication authentication) {
        PatientDTO myProfile = patientProxy.getPatientByAccount(authentication.getName());
        if (myProfile != null) {
            // Je mets l'image (en texte Base64) dans ma fiche et je sauvegarde
            myProfile.setProfilePicture(photoBase64);
            patientProxy.updatePatient(myProfile.getId(), myProfile);
        }
        return "redirect:/my-dossier";
    }
}