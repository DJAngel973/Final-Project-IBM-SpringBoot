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
        question.setId(nextId);
        questions.put(nextId, question);
        nextId++;
        return question;
    }
}