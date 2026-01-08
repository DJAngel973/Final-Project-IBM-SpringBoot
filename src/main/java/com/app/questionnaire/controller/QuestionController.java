package com.app.questionnaire.controller;

import org.springframework.ui.Model;
import com.app.questionnaire.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class QuestionController {

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
}