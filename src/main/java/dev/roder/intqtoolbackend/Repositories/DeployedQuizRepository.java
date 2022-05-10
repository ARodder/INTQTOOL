package dev.roder.intqtoolbackend.Repositories;

import dev.roder.intqtoolbackend.Entities.DeployedQuiz;
import dev.roder.intqtoolbackend.Entities.Notification;
import dev.roder.intqtoolbackend.Entities.QuizAnswer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for the deployedQuizzes.
 * Serves as interface between database and application.
 * Is auto-implemented into a Bean by Spring
 */
public interface DeployedQuizRepository extends CrudRepository<DeployedQuiz, Integer> {

    /**
     * Retrieves the quizAnswers for a specified deployment of a quiz
     * @param DeployedQuizId id of the deployment to retrieve answers from.
     * @return Returns a list of all the answers found for the specified deployment.
     */
    @Query(value="SELECT dp.quizAnswer FROM DeployedQuiz dp WHERE dp.id = ?1")
    List<QuizAnswer> findAnswersByDeployedQuizId(Integer DeployedQuizId);
}
