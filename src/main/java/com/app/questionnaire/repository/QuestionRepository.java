package com.app.questionnaire.repository;

import com.app.questionnaire.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Question entity.
 * Provides standard CRUD operations out of the box.
 */
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    // findAll(), findById(), save(), deleteById() are inherited from JpaRepository.
    // No custom methods needed for this MVP.
}
