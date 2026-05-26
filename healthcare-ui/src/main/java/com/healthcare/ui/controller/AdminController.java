package com.healthcare.ui.controller;

import com.healthcare.ui.dto.PatientDTO;
import com.healthcare.ui.dto.UserDTO;
import com.healthcare.ui.proxies.PatientProxy;
import com.healthcare.ui.proxies.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// Ici, c'est le coin de l'admin. Toutes les routes commencent par /admin
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserProxy userProxy; // Pour gérer les comptes utilisateurs (médecins/patients)

    @Autowired
    private PatientProxy patientProxy; // Pour voir les fiches médicales

    // J'AFFICHE SEULEMENT LA LISTE DES MÉDECINS
    @GetMapping("/users")
    public String listUsers(Model model) {
        // Je demande au proxy de me donner tout le monde
        List<UserDTO> allUsers = userProxy.getAllUsers();

        // Je ne garde que ceux qui ont le rôle "DOCTOR"
        List<UserDTO> doctorsOnly = allUsers.stream()
                .filter(u -> "DOCTOR".equals(u.getRole()))
                .toList();

        model.addAttribute("users", doctorsOnly);
        return "admin/user-list";
    }

    // SI JE VEUX SUPPRIMER UN COMPTE (MÉDECIN OU AUTRE)
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userProxy.deleteUser(id);
        return "redirect:/admin/users";
    }

    // ICI C'EST POUR VOIR TOUS LES PATIENTS DU SYSTÈME D'UN COUP
    @GetMapping("/patients-all")
    public String listAllPatients(Model model) {
        // 1. Je récupère d'abord tout ce qu'il y a dans la base des patients
        List<PatientDTO> allPatients = patientProxy.getAllPatients();

        // 2. J'AJOUTE LE FILTRE : Je retire les comptes qui ont "DOCTOR" dans leur nom
        // Ça permet d'avoir une liste propre avec seulement les vrais patients du système
        List<PatientDTO> patientsOnly = allPatients.stream()
                .filter(p -> !p.getLastName().toUpperCase().contains("DOCTOR"))
                .toList();

        model.addAttribute("patients", patientsOnly);

        // On pointe vers le fichier dans templates/admin/global-patient-list.html
        return "admin/global-patient-list";
    }
}