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
        if (!question.getOptions().contains(question.getCorrectAnswer())) {
            throw new IllegalArgumentException("The correct answer must be one of the options.");
        }
        if (question.getOptions().size() != question.getOptions().stream().distinct().count()) {
            throw new IllegalArgumentException("Options cannot have duplicates.");
        }
        question.setId(nextId);
        questions.put(nextId, question);
        nextId++;
        return question;
    }

    public Question editQuiz(Question question) {
        if (!questions.containsKey(question.getId())) {
            throw new IllegalArgumentException(String.format("Question with ID %d does not exist.", question.getId()));
        }
        questions.put(question.getId(), question);
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
        if (!updatedQuestion.getOptions().contains(updatedQuestion.getCorrectAnswer())) {
            throw new IllegalArgumentException("The correct answer must be one of the options.");
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
        String selectedAnswer = question.getOptions().get(selectedOptionIndex -1);
        boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(selectedAnswer.trim());
        userAnswersMap.computeIfAbsent(username, k -> new HashMap<>())
                .put(questionId, selectedAnswer);
        return isCorrect;
     }

     public List<AnsweredQuestion> getUserResults(String username) {
        if (!userAnswersMap.containsKey(username)) {
            throw new IllegalArgumentException("No results found for user: " + username);
        }
        Map<Integer, String> userAnswers = userAnswersMap.get(username);
        List<AnsweredQuestion> results = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            Question question = findQuestionById(entry.getKey());
            String userAnswer = entry.getValue();
            boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(userAnswer.trim());
            results.add(new AnsweredQuestion(question, username, userAnswer, isCorrect));
        }
     }
}