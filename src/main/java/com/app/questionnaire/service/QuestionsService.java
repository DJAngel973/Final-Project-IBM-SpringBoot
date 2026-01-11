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
}