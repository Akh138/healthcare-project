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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PatientSpaceController {

    @Autowired
    private PatientProxy patientProxy;

    @Autowired
    private NoteProxy noteProxy;

    /**
     * Affiche le dossier personnel du patient connecté.
     */
    @GetMapping("/my-dossier")
    public String showMyDossier(Model model, Authentication authentication) {
        String currentAccount = authentication.getName(); // "katya"

        // 1. On cherche LE dossier lié à ce compte (via la nouvelle route)
        PatientDTO myProfile = patientProxy.getPatientByAccount(currentAccount);

        // 2. Si aucun dossier n'est lié (ex: nouveau patient pas encore rattaché par SQL)
        if (myProfile == null) {
            return "patient/no-dossier";
        }

        // 3. Si dossier trouvé, on récupère les notes et messages NoSQL
        List<NoteDTO> notes = noteProxy.getNotesByPatient(myProfile.getId());
        List<MessageDTO> messages = noteProxy.getMessagesByPatient(myProfile.getId());

        model.addAttribute("patient", myProfile);
        model.addAttribute("notes", notes);
        model.addAttribute("messages", messages);
        model.addAttribute("newMessage", new MessageDTO());

        return "patient/my-dossier";
    }

    /**
     * Permet au patient d'envoyer un message à son médecin.
     */
    @PostMapping("/my-dossier/message")
    public String sendMessageToDoctor(@ModelAttribute("newMessage") MessageDTO message, Authentication authentication) {
        String currentAccount = authentication.getName();
        PatientDTO myProfile = patientProxy.getPatientByAccount(currentAccount);

        if (myProfile != null) {
            message.setPatientId(myProfile.getId());
            message.setSenderName(currentAccount); // On signe le message avec le pseudo (ex: "katya")
            noteProxy.sendMessage(message);
        }

        return "redirect:/my-dossier";
    }

    @PostMapping("/my-dossier/upload-photo")
    public String uploadPhoto(@RequestParam("photo") String photoBase64, Authentication authentication) {
        PatientDTO myProfile = patientProxy.getPatientByAccount(authentication.getName());
        if (myProfile != null) {
            myProfile.setProfilePicture(photoBase64);
            patientProxy.updatePatient(myProfile.getId(), myProfile);
        }
        return "redirect:/my-dossier";
    }

}