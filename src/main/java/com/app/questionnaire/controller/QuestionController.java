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

    @GetMapping("")
    public String showLandingPage() {
        return "home-landing";
    }

    // Get to retrieve the login page.
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    // Get to retrieve the registration page.
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Post to register user
    /**
     * Processes the registration of a new user in the system.
     * <p>Validates that the passwords match, registers the user, and displays a success or error message depending on the result</p>
     * @param user User object containing the form data
     * @param result result of the Bean Validation check
     * @param confirmPassword confirmation password from the form
     * @param model model user to pass attributes to the view
     * @return name of the "register" view with success or error messages
     * @throws IllegalArgumentException if the user already exists (handled internally)
     * */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result, @RequestParam String confirmPassword, Model model) {
        // Validate Bean Validation errors.
        if (result.hasErrors()) {
            return "register";
        }
        // Verify that the passwords match.
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "The passwords do not match.");
            return "register";
        }
        try {
            userDetailsService.registerUsers(
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRole()
            );
            String roleName = user.getRole().equals("ADMIN") ? "Administrator" : "User";
            model.addAttribute("success", String.format("Ok! User %s has been registered with role %s.", user.getUsername(), roleName));
            model.addAttribute("user", new User());
            return "register";
        } catch (IllegalArgumentException error) {
            model.addAttribute("error", "The user already exists");
            return "register";
        }
    }

    // Get to retrieve the add quizzes page.
    @GetMapping("/quizlist")
    public String showQuizList(Model model) {
        model.addAttribute("question", questionsService.loadQuizzes());
        return "quizlist";
    }
    @GetMapping("/quizlist/add")
    public String showAddQuestionnaire(Model model) {
        model.addAttribute("question", new Question());
        return "add-questionnaire";
    }

    // Get to retrieve the quiz editing page.
    @PostMapping("/quizlist/add")
    public String addQuestion(@Valid @ModelAttribute Question question, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-questionnaire";
        }
        try {
            questionsService.addQuiz(question);
            return "redirect:/quizlist?success";
        } catch (Exception error) {
            model.addAttribute("error", "Error saving the question.");
            model.addAttribute("question", question);
            return "add-questionnaire";
        }
    }

    // Get to retrieve the quiz editing page.
    @GetMapping("/quizlist/edit/{id}")
    public String showEditQuestionnaires(@PathVariable Integer id, Model model) {
        try {
            Question question = questionsService.findQuestionById(id);
            model.addAttribute("question", question);
            return "edit-questionnaire";
        } catch (Exception error) {
            model.addAttribute("error", "Question not found.");
            return "redirect:/quizlist";
        }
    }

    // Put to edit quiz questions - to implement HTML with Thymeleaf only apply GET and POST.
    @PostMapping("/quizlist/edit/{id}")
    public String editQuestions(@PathVariable Integer id, @Valid @ModelAttribute Question question, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("question", question);
            return  "edit-questionnaire";
        }
        try {
            questionsService.updateQuestion(id, question);
            return "redirect:/quizlist?updated";
        } catch (IllegalArgumentException error) {
            model.addAttribute("error", error.getMessage());
            model.addAttribute("question", question);
            return "edit-questionnaire";
        }
    }

    // Delete to remove quiz questions - POST is compatible with HTML and Thymeleaf.
    @PostMapping("/quizlist/delete/{id}")
    public String deleteQuestion(@PathVariable Integer id, Model model) {
        try {
            questionsService.deleteQuiz(id);
            return "redirect:/quizlist?deleted";
        } catch (IllegalArgumentException error) {
            model.addAttribute("error", error.getMessage());
            return "redirect:/quizlist?error";
        }
    }

    // Get to retrieve quiz questions, go to the home page, admin and user.
    @GetMapping("/quiz")
    public String showHome(Model model) {
        model.addAttribute("questions", questionsService.loadQuizzes());
        return "home";
    }

    // Post to send answers.
    @PostMapping("/quiz/answer/{username}")
    public String sendAnswer(@PathVariable String username,@PathVariable Integer id, @RequestParam Integer selectedOptionIndex, Model model) {
        try {
            boolean isCorrect = questionsService.validateAnswer(username, id, selectedOptionIndex);
            model.addAttribute("isCorrect", isCorrect);
            model.addAttribute("message", isCorrect ? "correct answer" : "isCorrect answer");
            return "redirect:/home?result=" + isCorrect;
        } catch (IllegalArgumentException error) {
            model.addAttribute("error", error.getMessage());
            return "redirect:/home?error";
        }
    }

    // Get to retrieve the results page.
    @GetMapping("/quiz/answer/result/{username}")
    public String resultAnswer(@PathVariable String username, Model model) {
        try {
            var results = questionsService.getUserResults(username);
            model.addAttribute("results",results);
            model.addAttribute("username", username);
            return "results";
        } catch (IllegalArgumentException error) {
            model.addAttribute("error", error.getMessage());
            return "redirect:home?error";
        }
    }
}