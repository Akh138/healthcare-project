package com.healthcare.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.userservice.model.User;
import com.healthcare.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ICI JE TESTE TOUTES LES ROUTES DE MON API (LE CONTROLLER).
 * Le but est d'avoir 100% de couverture pour être sûr que chaque ligne de mon code est testée.
 */
@WebMvcTest(UserController.class) // Je charge uniquement la couche Web pour que ce soit rapide
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // C'est mon simulateur de navigateur pour appeler mes URLs

    @MockBean
    private UserService userService; // Je simule le service pour ne pas toucher aux vraies données

    @Autowired
    private ObjectMapper objectMapper; // Petit outil pour transformer mes objets Java en texte JSON

    // --- TEST 1 : VÉRIFIER QUE LA LISTE DES USERS S'AFFICHE BIEN ---
    @Test
    public void testGetAllUsers_ShouldReturnList() throws Exception {
        // Je simule une liste avec 2 utilisateurs
        when(userService.getAllUsers()).thenReturn(Arrays.asList(new User(), new User()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk()) // On attend un code 200
                .andExpect(jsonPath("$.length()").value(2)); // On vérifie qu'on reçoit bien 2 users
    }

    // --- TEST 2 : VÉRIFIER LA RECHERCHE PAR ID QUAND ÇA MARCHE ---
    @Test
    public void testGetUserById_Success_ShouldReturnUser() throws Exception {
        User user = new User(1L, "test", "pwd", "ADMIN");
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"));
    }

    // --- TEST 3 : VÉRIFIER L'ERREUR 404 SI L'ID N'EXISTE PAS ---
    @Test
    public void testGetUserById_NotFound_ShouldReturn404() throws Exception {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound()); // On attend bien une erreur 404
    }

    // --- TEST 4 : VÉRIFIER LA CRÉATION D'UN UTILISATEUR (CODE 201) ---
    @Test
    public void testCreateUser_ShouldReturn201() throws Exception {
        User user = new User(1L, "new", "pwd", "DOCTOR");
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))) // On envoie l'objet en JSON
                .andExpect(status().isCreated()) // On vérifie qu'on a le code 201 (Created)
                .andExpect(jsonPath("$.username").value("new"));
    }

    // --- TEST 5 : VÉRIFIER LA MISE À JOUR (SUCCÈS) ---
    @Test
    public void testUpdateUser_ShouldReturnOk() throws Exception {
        User updatedUser = new User(1L, "updated", "pwd", "ADMIN");
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updated"));
    }

    // --- TEST 6 : VÉRIFIER LA MISE À JOUR QUAND L'USER N'EXISTE PAS (POUR LE 100%) ---
    @Test
    public void testUpdateUser_NotFound_ShouldReturn404() throws Exception {
        // ICI JE FORCE UNE ERREUR pour passer dans le bloc "catch" de mon controller
        when(userService.updateUser(eq(99L), any(User.class)))
                .thenThrow(new RuntimeException("Utilisateur non trouvé"));

        User anyUser = new User(99L, "fail", "pwd", "ADMIN");

        mockMvc.perform(put("/api/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(anyUser)))
                .andExpect(status().isNotFound()); // On vérifie que le catch renvoie bien 404
    }

    // --- TEST 7 : VÉRIFIER LA SUPPRESSION (CODE 204) ---
    @Test
    public void testDeleteUser_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent()); // On attend le code 204 (No Content)
    }

    // --- TEST 8 : VÉRIFIER LA RECHERCHE PAR PSEUDO ---
    @Test
    public void testSearchUser_ShouldReturnOk() throws Exception {
        User user = new User(1L, "admin", "pwd", "ADMIN");
        when(userService.getUserByUsername("admin")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/search/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }
}