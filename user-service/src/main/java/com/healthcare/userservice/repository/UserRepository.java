package com.healthcare.userservice.repository;

import com.healthcare.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Utile si on veut vérifier si un pseudo existe déjà
    Optional<User> findByUsername(String username);
}