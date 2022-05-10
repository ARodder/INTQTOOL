package dev.roder.intqtoolbackend.Repositories;

import org.springframework.data.repository.CrudRepository;

import dev.roder.intqtoolbackend.Entities.Question;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

/**
 * Repository for the Questions of Quizzes.
 * Serves as interface between database and application.
 * Is auto-implemented into a Bean by Spring
 */
public interface QuestionRepository extends CrudRepository<Question, Integer> {

}