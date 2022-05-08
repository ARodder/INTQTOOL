package dev.roder.intqtoolbackend.Repositories;

import dev.roder.intqtoolbackend.Entities.QuizAnswer;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the QuizAnswers.
 * Serves as interface between database and application.
 * Is auto-implemented into a Bean by Spring
 */
public interface QuizAnswerRepository extends CrudRepository<QuizAnswer, Integer> {

}
