package com.app.questionnaire.service;

import com.app.questionnaire.model.Role;
import com.app.questionnaire.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.core.userdetails.User.builder;

@Service
public class QuizUserDetailsService implements UserDetailsService {

    private List<User> users = new ArrayList<>();
    private final PasswordEncoder passwordEncoder;

    public QuizUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = findUserByUsername(username);

        return builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public User registerUsers(String username, String password, String email, Role role) {

        boolean userExists = users.stream()
                .anyMatch(u -> u.getUsername().equals(username));
        if(userExists) {
            throw new IllegalArgumentException("User already exists.");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEmail(email);
        newUser.setRole(role);

        users.add(newUser);
        return newUser;
    }

    public User findUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(()-> new UsernameNotFoundException("User not found."));
    }
}