package com.healthcare.userservice.repository;

import com.healthcare.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Cette méthode permet de chercher un médecin dans la base de données
    // "Optional" veut dire que le médecin peut exister ou non.
    Optional<User> findByUsername(String username);
}