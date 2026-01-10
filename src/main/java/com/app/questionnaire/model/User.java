package com.app.questionnaire.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class User {
    @NotBlank
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email address must be valid.")
    private String email;

    @NotBlank
    @Size(min = 13, message = "Minimum 13 characters.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "The password must contain uppercase letters, numbers, and special characters.")
    private String password;
    private String role;

    public User (String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(){}

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format("""
                User:
                username= %s
                role= %s
                """,username, role);
    }
}