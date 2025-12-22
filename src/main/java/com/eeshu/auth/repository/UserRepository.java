package com.eeshu.auth.repository;

import com.eeshu.auth.model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
     boolean existsByEmail(String email);
     boolean existsByUsername(String username);
     boolean existsByUsernameAndPassword(String username, String password);
     boolean existsByUsernameAndEmail(String username, String email);
     boolean existsByUsernameAndEmailAndPassword(String username, String email, String password);

}
