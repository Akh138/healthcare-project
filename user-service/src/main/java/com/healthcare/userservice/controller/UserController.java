package com.healthcare.userservice.controller;

import com.healthcare.userservice.model.User;
import com.healthcare.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTRÔLEUR POUR LA GESTION DES COMPTES (User Service)
 * C'est ici qu'on gère tout ce qui touche aux médecins, admins et patients.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // JE RÉCUPÈRE LA LISTE DE TOUS LES COMPTES (UTILE POUR L'ADMIN)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        // On renvoie un code 200 OK avec la liste complète
        return ResponseEntity.ok(users);
    }

    // JE RÉCUPÈRE UN COMPTE PRÉCIS PAR SON ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok) // Si le compte existe -> 200 OK
                .orElse(ResponseEntity.notFound().build()); // Sinon -> 404
    }

    // JE CRÉE UN NOUVEAU COMPTE (MÉDECIN, ADMIN OU PATIENT)
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        // TOUCHE PRO : On renvoie 201 Created pour dire que la création a réussi
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // JE METS À JOUR LES INFOS D'UN COMPTE
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // JE SUPPRIME UN COMPTE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        // TOUCHE PRO : On renvoie 204 No Content car l'objet n'existe plus
        return ResponseEntity.noContent().build();
    }

    /**
     * ROUTE SPÉCIALE : RECHERCHE PAR PSEUDO
     * Très important pour la sécurité et pour vérifier si un pseudo est déjà pris.
     */
    @GetMapping("/search/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok) // Si trouvé -> 200 OK
                .orElse(ResponseEntity.notFound().build()); // Si pas trouvé -> 404
    }
}