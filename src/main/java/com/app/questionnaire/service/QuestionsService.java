package com.app.questionnaire.service;

import com.app.questionnaire.model.Question;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class QuestionsService {

    private Map<Integer, Question> questions = new HashMap<>();
    private int nextId = 1;
    private Map<String, Map<Integer, String>> userAnswersMap = new HashMap<>();

    public List<Question> loadQuizzes() {
        return questions.values().stream().toList();
    }

    public Question addQuiz(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("The question cannot be null.");
        }
        if (question.getCorrectAnswer() == null ||
                question.getCorrectAnswer() < 1 ||
                question.getCorrectAnswer() > question.getOptions().size()) {
            throw new IllegalArgumentException("The correct answer must be between 1 and " + question.getOptions().size());
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
        if (updatedQuestion.getCorrectAnswer() == null ||
                updatedQuestion.getCorrectAnswer() < 1 ||
                updatedQuestion.getCorrectAnswer() > updatedQuestion.getOptions().size()) {
            throw new IllegalArgumentException("The correct answer must be between 1 and " + updatedQuestion.getOptions().size());
        }
        if (updatedQuestion.getOptions().size() != updatedQuestion.getOptions().stream().distinct().count()) {
            throw new IllegalArgumentException("Options cannot have duplicates.");
        }
        updatedQuestion.setId(id);
        questions.put(id, updatedQuestion);
        return updatedQuestion;
    }

     public boolean validateAnswer(String username, Integer questionId, Integer selectedOptionIndex) {
        Question question = findQuestionById(questionId);
        if (question == null) {
            throw new IllegalArgumentException("Question not found");
        }
        if (selectedOptionIndex == null || selectedOptionIndex < 1 || selectedOptionIndex > question.getOptions().size()) {
            throw new IllegalArgumentException("Invalid option selected");
        }
        boolean isCorrect = selectedOptionIndex.equals(question.getCorrectAnswer());
        String selectedAnswer = question.getOptions().get(selectedOptionIndex -1);
        userAnswersMap.computeIfAbsent(username, k -> new HashMap<>())
                .put(questionId, selectedAnswer);
        return isCorrect;
     }

     public Map<Question, Boolean> getUserResults(String username) {
        if (!userAnswersMap.containsKey(username)) {
            throw new IllegalArgumentException("No results found for user: " + username);
        }
        Map<Integer, String> userAnswers = userAnswersMap.get(username);
        Map<Question, Boolean> results = new HashMap<>();
        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            Question question = findQuestionById(entry.getKey());
            String userAnswer = entry.getValue();
            int userAnswerIndex = question.getOptions().indexOf(userAnswer) + 1;
            boolean isCorrect = (userAnswerIndex == question.getCorrectAnswer());
            results.put(question, isCorrect);
        }
        return results;
     }
}