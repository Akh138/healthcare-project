package com.healthcare.userservice;

import com.healthcare.userservice.model.User;
import com.healthcare.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import de l'encodeur

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // On crée l'encodeur

                User admin = new User();
                admin.setUsername("admin");
                // On crypte le mot de passe ici :
                admin.setPassword(encoder.encode("12345"));
                admin.setRole("ADMIN");

                userRepository.save(admin);
                System.out.println(">>> COMPTE ADMIN CRÉÉ AVEC BCRYPT : admin / 12345");
            }
        };
    }
}