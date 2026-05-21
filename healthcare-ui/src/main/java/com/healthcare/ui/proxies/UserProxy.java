package com.healthcare.ui.proxies;

import com.healthcare.ui.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// On appelle le microservice sur le port 8082
@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserProxy {

    // Pour l'inscription (déjà fait)
    @PostMapping("/api/users")
    UserDTO createUser(@RequestBody UserDTO user);

    // NOUVEAU : Pour le login. On va chercher un utilisateur par son nom.
    // L'UI demande au microservice l'utilisateur qui a ce "username"
    @GetMapping("/api/users/search/{username}")
    UserDTO getUserByUsername(@PathVariable("username") String username);
}