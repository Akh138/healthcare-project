package com.healthcare.ui.controller;

import com.healthcare.ui.model.UserDTO;
import com.healthcare.ui.proxies.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserProxy userProxy;

    @Autowired
    private PasswordEncoder passwordEncoder; // On injecte BCrypt ici

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") UserDTO user) {
        System.out.println("Inscription BCrypt pour : " + user.getUsername());

        try {
            // 1. ON HACHE LE MOT DE PASSE avant de l'envoyer
            // password123 devient par exemple : $2a$10$X87...
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            // 2. On envoie l'utilisateur avec son mot de passe crypté au microservice
            userProxy.createUser(user);
            System.out.println("Utilisateur enregistré avec succès (Mot de passe haché)");
        } catch (Exception e) {
            System.out.println("ERREUR Feign : " + e.getMessage());
        }

        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}