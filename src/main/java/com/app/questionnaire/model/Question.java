package com.app.questionnaire.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private Integer id;

    @NotBlank(message = "Question text is required.")
    private String questionText;

    @Size(min = 3, max = 4, message = "There should be between 3 and 4 options.")
    private List<String> options;

    @NotBlank(message = "The correct answer is required.")
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