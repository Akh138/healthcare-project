package com.healthcare.ui.controller;

import com.healthcare.ui.model.UserDTO;
import com.healthcare.ui.proxies.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserProxy userProxy;

    // Affiche le formulaire d'inscription
    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "signup";
    }

    // Reçoit les données du formulaire quand on clique sur "S'enregistrer"
    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") UserDTO user) {
        // LOG DE DEBUG : On vérifie dans la console d'IntelliJ si on reçoit bien l'utilisateur
        System.out.println("Tentative d'inscription pour l'utilisateur : " + user.getUsername());

        try {
            userProxy.createUser(user); // Envoi au microservice via Feign
            System.out.println("Inscription réussie via Feign !");
        } catch (Exception e) {
            System.out.println("ERREUR lors de l'appel au microservice : " + e.getMessage());
        }

        return "redirect:/login?success";
    }

    // On ajoute cette méthode pour que la redirection vers /login ne crash pas
    @GetMapping("/login")
    public String login() {
        return "login"; // Il faudra créer un fichier login.html plus tard
    }
}