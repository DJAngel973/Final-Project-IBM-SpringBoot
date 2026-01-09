package com.app.questionnaire.model;

public class User {
    private String username;

    @Email
    private String email;

    @NotBlank
    @Size(min = 13, message = "Minimum 13 characters.")
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