package com.app.questionnaire.service;

import com.app.questionnaire.model.Question;
import com.app.questionnaire.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for quiz question CRUD and user answer validation.
 * Question data is persisted in PostgreSQL via QuestionRepository.
 * User answers and results are stored in memory (session scope).
 */
@Service
public class QuestionsService {

    private final QuestionRepository questionRepository;

    // In-memory storage for active quiz sessions.
    // These are transient — they reset when the app restarts.
    // In a production system these would be persisted as well.
    private final Map<String, Map<Integer, String>> userAnswersMap = new HashMap<>();
    private final Map<String, Map<Question, Boolean>> userResultsMap = new HashMap<>();

    public QuestionsService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Retrieves all quiz questions from the database.
     * @return list of all questions
     */
    public List<Question> loadQuizzes() {
        return questionRepository.findAll();
    }

    /**
     * Adds a new question to the database.
     * Validates that the correct answer is present in the options list
     * and that options are unique.
     * @param question the question to add (id is ignored, JPA generates it)
     * @return the persisted question with generated ID
     */
    public Question addQuiz(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("The question cannot be null.");
        }
        if (question.getCorrectAnswer() == null || question.getCorrectAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("The correct answer cannot be empty.");
        }
        if (!question.getOptions().contains(question.getCorrectAnswer())) {
            throw new IllegalArgumentException("The correct answer must be one of the available options.");
        }
        if (question.getOptions().size() != question.getOptions().stream().distinct().count()) {
            throw new IllegalArgumentException("Options cannot have duplicates.");
        }

        // Let JPA generate the ID — set to null so it's treated as a new entity.
        question.setId(null);
        return questionRepository.save(question);
    }

    /**
     * Deletes a question by its ID.
     * @param id the question ID
     * @throws IllegalArgumentException if the question does not exist
     */
    public void deleteQuiz(Integer id) {
        if (!questionRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    String.format("Question with ID %d does not exist.", id));
        }
        questionRepository.deleteById(id);
    }

    /**
     * Finds a question by ID.
     * @param id the question ID
     * @return the question
     * @throws IllegalArgumentException if not found
     */
    public Question findQuestionById(Integer id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Question with ID %d does not exist.", id)));
    }

    /**
     * Updates an existing question with new data.
     * @param id              the question ID
     * @param updatedQuestion the new question data
     * @return the updated question
     */
    public Question updateQuestion(Integer id, Question updatedQuestion) {
        Question existing = findQuestionById(id);

        if (updatedQuestion.getCorrectAnswer() == null ||
                updatedQuestion.getCorrectAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("The correct answer cannot be empty.");
        }
        if (!updatedQuestion.getOptions().contains(updatedQuestion.getCorrectAnswer())) {
            throw new IllegalArgumentException("The correct answer must be one of the available options.");
        }
        if (updatedQuestion.getOptions().size() !=
                updatedQuestion.getOptions().stream().distinct().count()) {
            throw new IllegalArgumentException("Options cannot have duplicates.");
        }

        existing.setQuestionText(updatedQuestion.getQuestionText());
        existing.setOptions(updatedQuestion.getOptions());
        existing.setCorrectAnswer(updatedQuestion.getCorrectAnswer());

        return questionRepository.save(existing);
    }

    /**
     * Validates a user's answer to a question.
     * Stores the answer and result in memory.
     * @param username       the user answering
     * @param questionId     the question being answered
     * @param selectedAnswer the answer selected by the user
     * @return true if correct, false otherwise
     */
    public boolean validateAnswer(String username, Integer questionId, String selectedAnswer) {
        Question question = findQuestionById(questionId);
        boolean isCorrect = question.getCorrectAnswer().equals(selectedAnswer);

        Map<Integer, String> userAnswers =
                userAnswersMap.computeIfAbsent(username, k -> new HashMap<>());
        userAnswers.put(questionId, selectedAnswer);

        Map<Question, Boolean> results =
                userResultsMap.computeIfAbsent(username, k -> new HashMap<>());
        results.put(question, isCorrect);
        return isCorrect;
    }

    /**
     * Retrieves quiz results for a specific user.
     * @param username the user
     * @return map of questions to correctness (true/false)
     */
    public Map<Question, Boolean> getUserResults(String username) {
        if (!userResultsMap.containsKey(username)) {
            throw new IllegalArgumentException("No results found for user: " + username);
        }
        return userResultsMap.get(username);
    }
}