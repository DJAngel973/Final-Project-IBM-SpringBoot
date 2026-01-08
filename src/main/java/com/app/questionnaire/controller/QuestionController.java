package com.app.questionnaire.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class QuestionController {

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

}