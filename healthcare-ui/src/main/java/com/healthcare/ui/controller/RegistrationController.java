package com.healthcare.ui.controller;

import com.healthcare.ui.model.PatientDTO;
import com.healthcare.ui.model.UserDTO;
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
    private PatientProxy patientProxy; // Injecté pour créer la fiche patient

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new UserDTO());

        // On récupère les médecins pour la liste déroulante
        List<UserDTO> allUsers = userProxy.getAllUsers();
        List<UserDTO> doctors = allUsers.stream()
                .filter(u -> "DOCTOR".equals(u.getRole()))
                .toList();

        model.addAttribute("doctors", doctors);
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") UserDTO user,
                               @RequestParam(value = "selectedDoctor", required = false) String selectedDoctor) {
        try {
            // 1. On crée le compte utilisateur avec BCrypt
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userProxy.createUser(user);
            System.out.println("Compte utilisateur créé : " + user.getUsername());

            // 2. AUTOMATISATION : Si c'est un patient, on crée sa fiche médicale liée au médecin
            if ("PATIENT".equals(user.getRole()) && selectedDoctor != null && !selectedDoctor.isEmpty()) {
                PatientDTO newFiche = new PatientDTO();
                newFiche.setFirstName(user.getUsername()); // On utilise le pseudo par défaut
                newFiche.setLastName("NOUVEAU DOSSIER");
                newFiche.setBirthday("2000-01-01"); // Valeur par défaut
                newFiche.setGender("?");
                newFiche.setAddress("À renseigner");
                newFiche.setPhone("0000000000");
                newFiche.setEmail(user.getUsername() + "@healthcare.com");

                // On lie le patient au médecin sélectionné
                newFiche.setDoctorUsername(selectedDoctor);

                patientProxy.createPatient(newFiche);
                System.out.println("Fiche patient liée au Dr. " + selectedDoctor + " créée avec succès.");
            }

        } catch (Exception e) {
            System.out.println("ERREUR lors de l'inscription : " + e.getMessage());
        }
        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
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