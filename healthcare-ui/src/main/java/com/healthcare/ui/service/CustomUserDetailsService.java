package com.healthcare.ui.service;

import com.healthcare.ui.dto.UserDTO;
import com.healthcare.ui.proxies.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserProxy userProxy; // On injecte le Proxy pour parler au microservice

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. On appelle le microservice via le Proxy pour chercher l'utilisateur
        UserDTO userDTO = userProxy.getUserByUsername(username);

        // 2. Si l'utilisateur n'existe pas, on lance une erreur
        if (userDTO == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
        }

        // 3. On retourne un objet User que Spring Security comprend
        // Note : On ajoute "{noop}" devant le mot de passe pour dire à Spring
        // que le mot de passe en base n'est pas encore crypté (pour tes tests).
        return User.withUsername(userDTO.getUsername())
                .password(userDTO.getPassword())
                .roles(userDTO.getRole()) // Récupère DOCTOR ou ADMIN
                .build();
    }
}