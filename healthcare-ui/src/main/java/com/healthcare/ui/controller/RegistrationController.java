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

    // Ici on fait appel à nos proxies pour discuter avec les autres microservices
    @Autowired
    private UserProxy userProxy;

    @Autowired
    private PatientProxy patientProxy; // Utile pour créer la fiche médicale automatiquement à l'inscription

    @Autowired
    private PasswordEncoder passwordEncoder; // Pour crypter le mot de passe avant de l'enregistrer

    // C'est ici qu'on affiche la page pour s'inscrire
    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new UserDTO());

        // On va chercher tous les utilisateurs pour filtrer seulement ceux qui ont le rôle DOCTOR
        // Ça nous permet de remplir la liste déroulante dans le formulaire d'inscription
        List<UserDTO> allUsers = userProxy.getAllUsers();
        List<UserDTO> doctors = allUsers.stream()
                .filter(u -> "DOCTOR".equals(u.getRole()))
                .toList();

        model.addAttribute("doctors", doctors);

        // ATTENTION : On retourne maintenant "auth/signup" car le fichier est dans le dossier auth
        return "auth/signup";
    }

    // C'est ici qu'on traite les données une fois que l'utilisateur a cliqué sur "S'inscrire"
    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") UserDTO user,
                               @RequestParam(value = "selectedDoctor", required = false) String selectedDoctor) {
        try {
            // ÉTAPE 1 : On s'occupe de la création du compte utilisateur
            // On encode le mot de passe pour qu'il ne soit pas en clair dans la base de données
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userProxy.createUser(user);
            System.out.println("Compte utilisateur créé : " + user.getUsername());

            // ÉTAPE 2 : On automatise la création de la fiche patient
            // Si l'utilisateur choisit d'être un "PATIENT" et qu'un médecin est sélectionné, on lui crée son dossier médical par défaut
            if ("PATIENT".equals(user.getRole()) && selectedDoctor != null && !selectedDoctor.isEmpty()) {
                PatientDTO newFiche = new PatientDTO();
                newFiche.setFirstName(user.getUsername()); // Le pseudo devient le prénom par défaut
                newFiche.setLastName("NOUVEAU DOSSIER");
                newFiche.setBirthday("2000-01-01");
                newFiche.setGender("?");
                newFiche.setAddress("À renseigner");
                newFiche.setPhone("0000000000");
                newFiche.setEmail(user.getUsername() + "@healthcare.com");

                // On lie ce nouveau dossier au médecin choisi lors de l'inscription
                newFiche.setDoctorUsername(selectedDoctor);

                patientProxy.createPatient(newFiche);
                System.out.println("Fiche patient liée au Dr. " + selectedDoctor + " créée avec succès.");
            }

        } catch (Exception e) {
            // Si ça plante (ex: pseudo déjà pris), on l'affiche dans la console
            System.out.println("ERREUR lors de l'inscription : " + e.getMessage());
        }

        // Une fois fini, on renvoie vers la page de login avec un petit message de succès
        return "redirect:/login?success";
    }

    // Affiche la page de connexion
    @GetMapping("/login")
    public String login() {
        // Comme pour signup, on indique le chemin vers le dossier auth
        return "auth/login";
    }

    // Cette partie gère l'arrivée sur l'application après s'être connecté
    @GetMapping("/home")
    public String home(Authentication authentication) {
        // On récupère le rôle de la personne connectée
        String role = authentication.getAuthorities().toString();

        // Si c'est un patient, on ne veut pas qu'il voie l'accueil général, on le redirige vers son dossier
        if (role.contains("ROLE_PATIENT")) {
            return "redirect:/my-dossier";
        }

        // Sinon (pour les médecins et admins), on affiche la page home qui est à la racine des templates
        return "home";
    }
}