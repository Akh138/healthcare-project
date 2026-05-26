package com.healthcare.ui.controller;

import com.healthcare.ui.dto.PatientDTO;
import com.healthcare.ui.dto.UserDTO;
import com.healthcare.ui.proxies.PatientProxy;
import com.healthcare.ui.proxies.UserProxy;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RegistrationController {

    @Autowired
    private UserProxy userProxy;

    @Autowired
    private PatientProxy patientProxy;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================================================================
    // INSCRIPTION PUBLIQUE (Sert uniquement aux PATIENTS)
    // =========================================================================

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new UserDTO());

        // On récupère les médecins pour que le patient puisse choisir le sien
        List<UserDTO> doctors = userProxy.getAllUsers().stream()
                .filter(u -> "DOCTOR".equals(u.getRole()))
                .toList();

        model.addAttribute("doctors", doctors);
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") UserDTO user,
                               @RequestParam(value = "selectedDoctor", required = false) String selectedDoctor) {
        try {
            // SÉCURITÉ : On force le rôle à PATIENT pour les inscriptions publiques
            user.setRole("PATIENT");

            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userProxy.createUser(user);

            // On crée automatiquement le dossier médical lié au médecin choisi
            if (selectedDoctor != null && !selectedDoctor.isEmpty()) {
                PatientDTO newFiche = new PatientDTO();
                newFiche.setFirstName(user.getUsername());
                newFiche.setLastName("NOUVEAU DOSSIER");
                newFiche.setBirthday("2000-01-01");
                newFiche.setGender("?");
                newFiche.setAddress("À renseigner");
                newFiche.setPhone("0000000000");
                newFiche.setEmail(user.getUsername() + "@healthcare.com");
                newFiche.setDoctorUsername(selectedDoctor);

                patientProxy.createPatient(newFiche);
            }

        } catch (Exception e) {
            System.out.println("ERREUR lors de l'inscription patient : " + e.getMessage());
        }
        return "redirect:/login?success";
    }

    // =========================================================================
    // INSCRIPTION ADMIN (Sert à l'Admin pour créer des MÉDECINS ou d'autres ADMINS)
    // =========================================================================

    // Affiche le formulaire spécial pour l'admin
    @GetMapping("/admin/signup")
    public String showAdminSignUpForm(Model model) {
        model.addAttribute("user", new UserDTO());
        // On renvoie vers une nouvelle page qu'on va créer
        return "admin/admin-signup";
    }

    // Traite la création d'un membre du personnel par l'admin
    @PostMapping("/admin/signup")
    public String registerStaffByAdmin(@ModelAttribute("user") UserDTO user) {
        try {
            // On crypte le mot de passe du nouveau collègue
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            // On crée le compte (le rôle est celui choisi par l'admin dans le formulaire)
            userProxy.createUser(user);
            System.out.println("Nouveau membre du personnel créé : " + user.getUsername() + " (" + user.getRole() + ")");

        } catch (Exception e) {
            System.out.println("ERREUR lors de la création par l'admin : " + e.getMessage());
        }
        // Une fois créé, on ramène l'admin vers la liste des médecins
        return "redirect:/admin/users?created";
    }

    // =========================================================================
    // CONNEXION ET REDIRECTION
    // =========================================================================

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/home")
    public String home(Authentication authentication) {
        String role = authentication.getAuthorities().toString();
        if (role.contains("ROLE_PATIENT")) {
            return "redirect:/my-dossier";
        }
        return "home";
    }
}