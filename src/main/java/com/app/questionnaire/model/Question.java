package com.app.questionnaire.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private Integer id;
    private String questionText;
    private List<String> options;
    private String correctAnswer;

    public Question(Integer id, String questionText, List<String> options, String correctAnswer) {
        this.id = id;
        this.questionText = questionText;
        this.options = new ArrayList<>(options);
        this.correctAnswer = correctAnswer;
    }

    public Question() {
        this.options = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }
    public void setOptions(List<String> options) {
        this.options = new ArrayList<>(options);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String toString() {
        return String.format("""
                Question:
                Question text = %s
                Options = %s
                Correct answer = %s
                """, questionText, options, correctAnswer);
    }
}