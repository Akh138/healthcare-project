package com.healthcare.ui.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // On désactive le CSRF pour que nos formulaires POST fonctionnent facilement
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // On autorise l'accès libre aux pages de base
                        .requestMatchers("/signup", "/login", "/css/**", "/js/**").permitAll()
                        // Tout le reste demande d'être connecté
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")             // Notre page de login
                        .defaultSuccessUrl("/home", true) // Redirection vers home après connexion
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // ON UTILISE BCRYPT : Le standard pro pour hacher les mots de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}