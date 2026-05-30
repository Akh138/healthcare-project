package com.healthcare.userservice.integration;

import com.healthcare.userservice.model.User;
import com.healthcare.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ICI JE TESTE MON MICROSERVICE EN ENTIER (INTEGRATION).
 * Je n'utilise pas de "Mock" pour le service, je teste la vraie communication
 * entre le Controller, le Service et la Base de données (H2).
 */
@SpringBootTest // Je demande à Spring de lancer toute l'application pour le test
@AutoConfigureMockMvc // Pour pouvoir simuler les appels HTTP sur mes vraies routes
@ActiveProfiles("test") // TRÈS IMPORTANT : On utilise H2 et non MySQL (fichier application-test.properties)
@Transactional // Après le test, tout est effacé de la base H2 pour ne pas salir les tests suivants
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Mon simulateur d'appels HTTP

    @Autowired
    private UserRepository userRepository; // J'injecte le VRAI repository pour écrire en base

    @Test
    public void testFullFlow_SearchUserInDatabase() throws Exception {
        // --- 1. PRÉPARER (Arrange) ---
        // J'enregistre un VRAI utilisateur dans ma base de test H2
        User realUser = new User(null, "integration_user", "password", "DOCTOR");
        userRepository.save(realUser);

        // --- 2. AGIR ET VÉRIFIER (Act & Assert) ---
        // Je fais un vrai appel sur l'URL pour voir si le système arrive à lire dans la base
        mockMvc.perform(get("/api/users/search/integration_user"))
                .andExpect(status().isOk()) // Je vérifie que j'ai bien un code 200
                .andExpect(jsonPath("$.username").value("integration_user")) // Je vérifie que le nom est le bon
                .andExpect(jsonPath("$.role").value("DOCTOR")); // Et que le rôle est respecté
    }
}