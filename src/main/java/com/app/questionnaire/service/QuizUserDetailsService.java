package com.app.questionnaire.service;

import com.app.questionnaire.model.Role;
import com.app.questionnaire.model.User;
import com.app.questionnaire.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static org.springframework.security.core.userdetails.User.builder;

/**
 * User management service for the questionnaire system.
 * Implements Spring Security's UserDetailsService for authentication.
 * Uses PostgreSQL via UserRepository instead of in-memory storage.
 */
@Service
public class QuizUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public QuizUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Loads a user by username for Spring Security authentication.
     * @param username the username to look up
     * @return UserDetails with encoded password and role
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    /**
     * Registers a new user in the database.
     * @param username unique username
     * @param password raw password (will be BCrypt-encoded)
     * @param email    user's email address
     * @param role     ADMIN or USER
     * @return the persisted User
     * @throws IllegalArgumentException if the username already exists
     */
    public User registerUsers(String username, String password, String email, Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User already exists.");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEmail(email);
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    /**
     * Finds a user by username.
     * @param username the username to look up
     * @return the User entity
     * @throws UsernameNotFoundException if not found
     */
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}