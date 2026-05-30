package com.healthcare.healthcareui.controller; // Ton dossier exact

import com.healthcare.ui.HealthcareUiApplication;
import com.healthcare.ui.configuration.SecurityConfig; // AJOUTÉ
import com.healthcare.ui.controller.RegistrationController;
import com.healthcare.ui.proxies.PatientProxy;
import com.healthcare.ui.proxies.UserProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import; // AJOUTÉ
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TEST DES REDIRECTIONS (BONUS UI).
 * J'ai ajouté @Import(SecurityConfig.class) pour que Spring utilise
 * ma propre page de login et pas celle par défaut.
 */
@WebMvcTest(RegistrationController.class)
@ContextConfiguration(classes = HealthcareUiApplication.class)
@Import(SecurityConfig.class) // RÉPARATION : On force l'utilisation de MA sécurité
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProxy userProxy;

    @MockBean
    private PatientProxy patientProxy;

    @MockBean
    private PasswordEncoder passwordEncoder;

    // --- TEST 1 : UN PATIENT VA SUR SON DOSSIER ---
    @Test
    @WithMockUser(username = "katya", roles = {"PATIENT"})
    public void testHome_AsPatient_ShouldRedirectToMyDossier() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-dossier"));
    }

    // --- TEST 2 : UN ADMIN ARRIVE SUR L'ACCUEIL ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testHome_AsAdmin_ShouldShowHomePage() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    // --- TEST 3 : LA PAGE DE LOGIN EST ACCESSIBLE ---
    @Test
    public void testLoginPage_ShouldBeAccessibleByAll() throws Exception {
        // Cette fois-ci, comme on a importé SecurityConfig,
        // Spring va bien appeler mon contrôleur pour /login
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }
}