package dev.roder.intqtoolbackend.Repositories;

import dev.roder.intqtoolbackend.Entities.QuestionAnswer;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the QuestionAnswers of QuizAnswers.
 * Serves as interface between database and application.
 * Is auto-implemented into a Bean by Spring
 */
public interface QuestionAnswerRepository extends CrudRepository<QuestionAnswer, Integer> {
}
