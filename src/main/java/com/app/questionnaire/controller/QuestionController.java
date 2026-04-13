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
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class QuestionController {

    private final QuizUserDetailsService userDetailsService;
    private final QuestionsService questionsService;

    public QuestionController(QuizUserDetailsService userDetailsService, QuestionsService questionsService) {
        this.userDetailsService = userDetailsService;
        this.questionsService = questionsService;
    }

    /**
     * Displays the landing page of the application.
     * <p>This is the main entry point for users visiting the application.</p>
     * @return name of the "home-landing" view
     * */
    @GetMapping("")
    public String showLandingPage() {
        return "home-landing";
    }

    /**
     * Displays the login page.
     * <p>Retrieves the authentication form where users can enter their credentials.</p>
     * @return name of the "login" view
     * */
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    /**
     * Displays the registration page.
     * <p>Initializes an empty User object and adds it to the model for form binding.</p>
     * @param model model to pass attributes to the view
     * @return name of the "register" view
     * */
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
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result, @RequestParam String confirmPassword, Model model, RedirectAttributes redirectAttributes) {
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
            String roleName = user.getRole().name().equalsIgnoreCase("ADMIN") ? "Administrator" : "User";
            redirectAttributes.addFlashAttribute("success", String.format("Ok! user %s has been registered with role %s.", user.getUsername(), roleName));
            return "redirect:/register";
        } catch (IllegalArgumentException error) {
            model.addAttribute("error", "The user already exists");
            return "register";
        }
    }

    /**
     * Displays the list of all quiz questions.
     * <p>Loads all available quizzes from the service and passes them to the view.</p>
     * @param model model to pass attributes to the vivew
     * @return name of the "quizlist" view
     * */
    @GetMapping("/quizlist")
    public String showQuizList(Model model) {
        model.addAttribute("questions", questionsService.loadQuizzes());
        return "quizlist";
    }

    /**
     * Displays the form to add a new quiz question.
     * <p>Initializes an empty Question object for form binding.</p>
     * @param model model to pass attributes to the view
     * @return name of the "add-questionnaire" view
     * */
    @GetMapping("/quizlist/add")
    public String showAddQuestionnaire(Model model) {
        model.addAttribute("question", new Question());
        return "add-questionnaire";
    }

    /**
     * processes the addition of a new quiz question.
     * <p>Validates the question data and saves it to the database. Redirects to the quiz list on success ot returns to the form with errors.</p>
     * @param question Question object containing the form data
     * @param result result of the Bean Validation check
     * @param model model to pass attributes to the view
     * @return redirect to "/quizlist?success" on success, or "add-questionnaire" view on error
     * */
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

    /**
     * Displays the form to edit an existing quiz question.
     * <p>Retrieves the question by ID and loads it into form for editing.</p>
     * @param id ID of the question to edit
     * @param model model to pass attributes to the view
     * @return name of the "edit-questionnaire" view, or redirect to "/quizlist" if question not found
     * */
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

    /**
     * Processes the update of an existing quiz question.
     * <p>Validates the updated data and saves changes to the database. Uses POST method for HTML/Thymeleaf compatibility instead of PUT.</p>
     * @param id ID of the question to update
     * @param question Question object containing the updated form data
     * @param result result of the Bean Validation check
     * @param model model to pass attribute to the view
     * @return redirect to "/quizlist?updated" on success, or "edit-questionnaire" view on error
     * */
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

    /**
     * Processes the deletion of a quiz question.
     * <p>Removes the question from the database by ID. Uses POST method for HTML/Thymeleaf compatibility instead of DELETE.</p>
     * @param id ID of the question to delete
     * @param model model to pass attributes to the view
     * @return redirect to "/quizlist?deleted" on success, or "/quizlist?error" on failure
     * */
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

    /**
     * Displays the quiz page with shuffled questions.
     * <p>Loads all questions, shuffles their options randomly, and displays them to the authenticated user.</p>
     * @param model model to pass to attributes to the view
     * @param principal authenticated user information
     * @return name of the "home" view
     * */
    @GetMapping("/quiz")
    public String showHome(Model model, Principal principal) {
        List<Question> questions = questionsService.loadQuizzes();
        List<Question> shuffledQuestions = questions.stream()
                .map(q -> {
                    Question shuffled = new Question();
                    shuffled.setId(q.getId());
                    shuffled.setQuestionText(q.getQuestionText());
                    shuffled.setCorrectAnswer(q.getCorrectAnswer());
                    List<String> shuffledOptions = new ArrayList<>(q.getOptions());
                    Collections.shuffle(shuffledOptions);
                    shuffled.setOptions(shuffledOptions);
                    return shuffled;
                })
                .collect(Collectors.toList());
        model.addAttribute("questions", shuffledQuestions);
        model.addAttribute("username", principal.getName());
        return "home";
    }

    /**
     * Processes the submission of quiz answers.
     * <p>Validates each answer submitted by the user and stores the results in the system.</p>
     * @param answers map containing question IDs and selected answers
     * @param principal authenticated user information
     * @return redirect to "/quiz/results" on success, or "/quiz?error" on failure
     * */
    @PostMapping("/quiz/submit")
    public String submitQuiz(@RequestParam Map<String, String> answers, Principal principal) {
        String username = principal.getName();
        try {
            for (Map.Entry<String, String> entry : answers.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("answer_")) {
                    Integer questionId = Integer.parseInt(key.replace("answer_", ""));
                    String selectedAnswer = entry.getValue();
                    questionsService.validateAnswer(username, questionId, selectedAnswer);
                }
            }
            return "redirect:/quiz/results";
        } catch (Exception error) {
            return "redirect:/quiz?error";
        }
    }

    /**
     * Displays the quiz results page.
     * <p>Shows the user's answers, calculates the score, and displays statistics including correct answers and percentage.</p>
     * @param model model to pass attributes to the view
     * @param principal authenticated usr information
     * @return name of the "result" view, or redirect to "/quiz?error" if results cannot be loaded
     * */
    @GetMapping("/quiz/results")
    public String showResults(Model model, Principal principal) {
        String username = principal.getName();
        try {
            Map<Question, Boolean> results = questionsService.getUserResults(username);
            long correctAnswers = results.values().stream().filter(Boolean::booleanValue).count();
            int totalQuestions = results.size();
            double score = (correctAnswers * 100.0) / totalQuestions;
            model.addAttribute("results", results);
            model.addAttribute("username", username);
            model.addAttribute("correctAnswers", correctAnswers);
            model.addAttribute("totalQuestions",totalQuestions);
            model.addAttribute("score", score);
            model.addAttribute("scoreFormatted", String.format("%.2f", score));
            return "result";
        } catch (IllegalArgumentException error) {
            model.addAttribute("error", error.getMessage());
            return "redirect:/quiz?error";
        }
    }
}