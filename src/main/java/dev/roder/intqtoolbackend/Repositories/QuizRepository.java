package dev.roder.intqtoolbackend.Repositories;

import org.springframework.data.repository.CrudRepository;

import dev.roder.intqtoolbackend.Entities.Quiz;
        ;import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
/**
 * Repository for the Quizzes.
 * Serves as interface between database and application.
 * Is auto-implemented into a Bean by Spring
 */
public interface QuizRepository extends CrudRepository<Quiz, Integer> {

        /**
         * Retrieves a specific quiz based on the given id
         *
         * @param quizID id of the quiz to find
         * @return Returns optional containing the quiz if it exists in the database.
         */
        Optional<Quiz> findByQuizID(Integer quizID);
}