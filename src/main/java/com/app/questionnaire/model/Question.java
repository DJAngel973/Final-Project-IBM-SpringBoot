package com.app.questionnaire.model;

import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

public class Question {
    private Integer id;

    @NotBlank(message = "Question text is required.")
    private String questionText;

    @NotNull(message = "Options are required")
    @Size(min = 4, max = 4, message = "Must have exactly 4 options")
    private List<String> options;

    @NotNull(message = "The correct answer is required.")
    @Min(value = 1, message = "Correct answer must be between 1 and 4")
    @Max(value = 4, message = "Correct answer must be between 1 and 4")
    private Integer correctAnswer;

    public Question(Integer id, String questionText, List<String> options, Integer correctAnswer) {
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

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(Integer correctAnswer) {
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