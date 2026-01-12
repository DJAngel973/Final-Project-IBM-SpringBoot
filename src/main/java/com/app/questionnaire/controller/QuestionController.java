package com.app.questionnaire.controller;

import com.app.questionnaire.model.Question;
import com.app.questionnaire.service.QuestionsService;
import com.app.questionnaire.service.QuizUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.app.questionnaire.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class QuestionController {

    private final QuizUserDetailsService userDetailsService;
    private final QuestionsService questionsService;

    public QuestionController(QuizUserDetailsService userDetailsService, QuestionsService questionsService) {
        this.userDetailsService = userDetailsService;
        this.questionsService = questionsService;
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
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result, @RequestParam String confirmPassword, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "The passwords do not match.");
            return "register";
        }
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

    // Get para recuperar la página de agregar cuestionarios
    @GetMapping("/questionnaires")
    public String showAddQuestionnaires(Model model) {
        model.addAttribute("question", new Question());
        return "questionnaires";
    }

    // Post para agregar preguntas de cuestionarios
    @PostMapping("/questionnaires")
    public String addQuestion(@Valid @ModelAttribute Question question, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "questionnaires";
        }
        try {
            questionsService.addQuiz(question);
            return "redirect:/questionnaires?success";
        } catch (Exception error) {
            model.addAttribute("error", "Error saving the question.");
            model.addAttribute("question", question);
            return "questionnaires";
        }
    }

    // Get para recuperar la página de editar cuestionarios
    @GetMapping("/questionnaires/edit/{id}")
    public String showEditQuestionnaires(@PathVariable Integer id, Model model) {
        try {
            Question question = questionsService.findQuestionById(id);
            model.addAttribute("question", question);
            return "edit-questionnaire";
        } catch (Exception error) {
            model.addAttribute("error", "Question not found.");
            return "redirect:/questionnaires";
        }
    }

    // Put para editar preguntas de cuestionarios - para implementar el HTML con Thymeleaf solo aplica GET y POST.
    @PostMapping("/questionnaires/edit/{id}")
    public String editQuestions(@PathVariable Integer id, @Valid @ModelAttribute Question question, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("question", question);
            return  "edit-questionnaire";
        }
        try {
            questionsService.updateQuestion(id, question);
            return "redirect:/ questionnaire?updated";
        } catch (IllegalArgumentException error) {
            model.addAttribute("error", error.getMessage());
            model.addAttribute("question", question);
            return "edit-questionnaire";
        }
    }
}