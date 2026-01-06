package com.app.questionnaire.service;

import com.app.questionnaire.model.Question;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class QuestionsService {

    private Map<Integer, Question> questions = new HashMap<>();

    public List<Question> loadQuizzes() {
        return questions.values().stream().toList();
    }
}