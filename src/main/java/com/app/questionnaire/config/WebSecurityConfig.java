package com.app.questionnaire.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the questionnaire application
 * This class defines authentication, authorization rules, and password encoding,
 * and security filter chain configuration.
 * @Configuration marks this class as a source of bean definitions
 * @EnableWebSecurity enables Spring Security's web security support
 * */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Creates and configures a password encoder bean for secure password storage.
     * Uses BCrypt hashing algorithm which is a strong, adaptive hashing function
     * designed specifically for password hashing.
     * @return BCryptPasswordEncoder instance that will be used throughout the application to
     * hash passwords before storing them in the database
     * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login").permitAll()
                        .requestMatchers("/quizlist/**").hasRole("ADMIN")
                        .requestMatchers("/quiz/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
                            String redirectUrl = isAdmin ? "/quizlist" : "/quiz";
                            response.sendRedirect(redirectUrl);
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/?logout")
                        .permitAll()
                );
        return http.build();
    }
}