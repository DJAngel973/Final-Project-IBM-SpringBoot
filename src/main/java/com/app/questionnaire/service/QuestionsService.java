package com.app.questionnaire.service;

import com.app.questionnaire.model.Question;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class QuestionsService {

    private Map<Integer, Question> questions = new HashMap<>();
    private int nextId = 1;
    private Map<String, Map<Integer, String>> userAnswersMap = new HashMap<>();
    private Map<String, Map<Question, Boolean>> userResultsMap = new HashMap<>();

    public List<Question> loadQuizzes() {
        return questions.values().stream().toList();
    }

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

    public Question deleteQuiz(Integer id) {
        if (!questions.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Question with ID %d does not exist.", id));
        }
        return questions.remove(id);
    }

    public Question findQuestionById(Integer id) {
        if (!questions.containsKey(id)) {
            throw new IllegalArgumentException((String.format("Question with ID %d does not exist.",id)));
        }
        return questions.get(id);
    }

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

     public boolean validateAnswer(String username, Integer questionId, String selectedAnswer) {
        Question question = findQuestionById(questionId);
        boolean isCorrect = question.getCorrectAnswer().equals(selectedAnswer);
        Map<Integer, String> userAnswers = userAnswersMap.computeIfAbsent(username, k -> new HashMap<>());
        userAnswers.put(questionId, selectedAnswer);
        Map<Question, Boolean> results = userResultsMap.computeIfAbsent(username, k -> new HashMap<>());
        results.put(question, isCorrect);
        return isCorrect;
     }

     public Map<Question, Boolean> getUserResults(String username) {
        if (!userResultsMap.containsKey(username)) {
            throw new IllegalArgumentException("No results found for user: " + username);
        }
        return userResultsMap.get(username);
     }
}