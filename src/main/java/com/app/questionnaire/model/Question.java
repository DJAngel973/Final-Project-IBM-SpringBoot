package com.app.questionnaire.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private String questionText;
    private List<String> options;
    private String correctAnswer;

    public Question(int id, String questionText, List<String> options, String correctAnswer) {
        this.id = id;
        this.questionText = questionText;
        this.options = new ArrayList<>();
        this.correctAnswer = correctAnswer;
    }

    public Question() {
        this.options = new ArrayList<>();
    }
    
}