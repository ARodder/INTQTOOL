package dev.roder.intqtoolbackend.Repositories;

import org.springframework.data.repository.CrudRepository;

import dev.roder.intqtoolbackend.Entities.Quiz;
        ;import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface QuizRepository extends CrudRepository<Quiz, Integer> {

        Optional<Quiz> findByQuizID(Integer quizID);
}