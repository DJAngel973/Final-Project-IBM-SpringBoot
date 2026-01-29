package com.app.questionnaire.service;

import com.app.questionnaire.model.Question;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Service class that manages quiz questions and user answers.
 * Provides operations for CRUD actions in questions and tracking user responses.
 * */
@Service
public class QuestionsService {

    private Map<Integer, Question> questions = new HashMap<>();
    private int nextId = 1;
    private Map<String, Map<Integer, String>> userAnswersMap = new HashMap<>();
    private Map<String, Map<Question, Boolean>> userResultsMap = new HashMap<>();

    /**
     * Retrieves all available quiz questions.
     * @return a list of all questions in the system
     * */
    public List<Question> loadQuizzes() {
        return questions.values().stream().toList();
    }

    /**
     * Adds a new question to the quiz system.
     * Validates that the question has a correct answer, the answer exists in options, and all options are unique.
     * @param question the question to be added
     * @return the added question with assigned ID
     * @throws IllegalArgumentException if question is null, correct answer is empty, correct answer is not in options, or options contain duplicates
     * */
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
        question.setId(nextId);
        questions.put(nextId, question);
        nextId++;
        return question;
    }

    /**
     * Deletes a question from the system by ist ID.
     * @param id the ID of the question to delete
     * @return the deleted question
     * @throws IllegalArgumentException if no question exists with the given ID
     * */
    public Question deleteQuiz(Integer id) {
        if (!questions.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Question with ID %d does not exist.", id));
        }
        return questions.remove(id);
    }

    /**
     * Finds and returns a question by its ID.
     * @param id the ID of the question to find
     * @return the question with the specified ID
     * @throws IllegalArgumentException if no question exists with the given ID
     * */
    public Question findQuestionById(Integer id) {
        if (!questions.containsKey(id)) {
            throw new IllegalArgumentException((String.format("Question with ID %d does not exist.",id)));
        }
        return questions.get(id);
    }

    /**
     * Updates an existing question with new information.
     * Validates the updated question's correct answer and options.
     * @param id the ID of the question to update
     * @param updatedQuestion the new question data
     * @return the updated question
     * @throws IllegalArgumentException if question doesn't exist, correct answer is empty, correct answer is not in options, or options contain duplicates
     * */
    public Question updateQuestion(Integer id, Question updatedQuestion) {
        if (!questions.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Question with ID %d does not exist.",id));
        }
        if (updatedQuestion.getCorrectAnswer() == null || updatedQuestion.getCorrectAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("The correct answer cannot be empty.");
        }
        if (!updatedQuestion.getOptions().contains(updatedQuestion.getCorrectAnswer())) {
            throw new IllegalArgumentException("The correct answer must be one of the available options.");
        }
        if (updatedQuestion.getOptions().size() != updatedQuestion.getOptions().stream().distinct().count()) {
            throw new IllegalArgumentException("Options cannot have duplicates.");
        }
        updatedQuestion.setId(id);
        questions.put(id, updatedQuestion);
        return updatedQuestion;
    }

    /**
     * Validates a user's answer to a specific question.
     * Stores the answer and result for the user.
     * @param username the username of the person answering
     * @param questionId the ID of the question being answered
     * @param selectedAnswer the answer selected by the user
     * @return true if the answer is correct, false otherwise
     * @throws IllegalArgumentException if the question doesn't exist
     * */
     public boolean validateAnswer(String username, Integer questionId, String selectedAnswer) {
        Question question = findQuestionById(questionId);
        boolean isCorrect = question.getCorrectAnswer().equals(selectedAnswer);
        Map<Integer, String> userAnswers = userAnswersMap.computeIfAbsent(username, k -> new HashMap<>());
        userAnswers.put(questionId, selectedAnswer);
        Map<Question, Boolean> results = userResultsMap.computeIfAbsent(username, k -> new HashMap<>());
        results.put(question, isCorrect);
        return isCorrect;
     }

     /**
      * Retrieves all quiz results for a specific user.
      * @param username the username whose results to retrieve
      * @return a map of questions to their correctness (true/false)
      * @throws IllegalArgumentException if no results exist for the user
      * */
     public Map<Question, Boolean> getUserResults(String username) {
        if (!userResultsMap.containsKey(username)) {
            throw new IllegalArgumentException("No results found for user: " + username);
        }
        return userResultsMap.get(username);
     }
}