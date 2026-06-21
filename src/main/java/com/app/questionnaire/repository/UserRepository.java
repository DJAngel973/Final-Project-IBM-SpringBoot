package com.app.questionnaire.repository;

import com.app.questionnaire.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 * Method names follow Spring Data's query derivation convention.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by username for authentication.
     * Spring Data generates the query automatically: SELECT ... WHERE username = ?
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a username is already taken.
     */
    boolean existsByUsername(String username);
}
