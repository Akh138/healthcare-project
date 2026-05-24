package com.healthcare.ui.controller;

import com.healthcare.ui.model.PatientDTO;
import com.healthcare.ui.model.UserDTO;
import com.healthcare.ui.proxies.PatientProxy;
import com.healthcare.ui.proxies.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin") // Toutes les URLs commenceront par /admin/...
public class AdminController {

    @Autowired
    private UserProxy userProxy;

    @Autowired
    private PatientProxy patientProxy;

    // AFFICHER LA LISTE DES MÉDECINS
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserDTO> users = userProxy.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user-list"; // Cherchera templates/admin/user-list.html
    }

    // SUPPRIMER UN MÉDECIN
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userProxy.deleteUser(id);
        return "redirect:/admin/users";
    }

    // VOIR TOUS LES PATIENTS (GLOBAL)
    @GetMapping("/patients-all")
    public String listAllPatients(Model model) {
        //  utilise la méthode getAllPatients (sans filtre par médecin)
        List<PatientDTO> patients = patientProxy.getAllPatients();
        model.addAttribute("patients", patients);
        return "admin/global-patient-list";
    }
}