package com.app.questionnaire.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a quiz question with four options and one correct answer.
 * Options are stored in a separate table via @ElementCollection.
 */
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Question text is required.")
    private String questionText;

    @NotNull(message = "Options are required")
    @Size(min = 4, max = 4, message = "Must have exactly 4 options")
    @ElementCollection
    @CollectionTable(
        name = "question_options",
        joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "option_text")
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

    // ── Getters and setters ──

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

    @Override
    public String toString() {
        return String.format("""
                Question:
                Question text = %s
                Options = %s
                Correct answer = %s
                """, questionText, options, correctAnswer);
    }
}