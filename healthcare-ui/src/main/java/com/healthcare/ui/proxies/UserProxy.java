package com.healthcare.ui.proxies;

import com.healthcare.ui.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserProxy {

    //  Inscription
    @PostMapping("/api/users")
    UserDTO createUser(@RequestBody UserDTO user);

    //  Login
    @GetMapping("/api/users/search/{username}")
    UserDTO getUserByUsername(@PathVariable("username") String username);

    // Récupérer TOUS les médecins pour l'Admin
    @GetMapping("/api/users")
    List<UserDTO> getAllUsers();

    // Permettre à l'Admin de supprimer un médecin
    @DeleteMapping("/api/users/{id}")
    void deleteUser(@PathVariable("id") Long id);
}