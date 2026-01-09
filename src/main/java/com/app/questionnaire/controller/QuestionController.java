package com.app.questionnaire.controller;

import com.app.questionnaire.service.QuizUserDetailsService;
import org.springframework.ui.Model;
import com.app.questionnaire.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class QuestionController {

    private final QuizUserDetailsService userDetailsService;

    public QuestionController(QuizUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Get para recuperar la pagina de inicio de sesión.
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    // Get para recuperar la pagina de registro.
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Post para registrar usuario
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            userDetailsService.registerUsers(
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    "USER"
            );
            return "redirect:/login";
        } catch (IllegalArgumentException error) {
            model.addAttribute("error", "The user already exists");
            return "register";
        }
    }
}