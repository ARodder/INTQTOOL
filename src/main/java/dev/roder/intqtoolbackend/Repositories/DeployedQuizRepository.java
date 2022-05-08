package dev.roder.intqtoolbackend.Repositories;

import dev.roder.intqtoolbackend.Entities.DeployedQuiz;
import dev.roder.intqtoolbackend.Entities.Notification;
import dev.roder.intqtoolbackend.Entities.QuizAnswer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeployedQuizRepository extends CrudRepository<DeployedQuiz, Integer> {

    @Query(value="SELECT dp.quizAnswer FROM DeployedQuiz dp WHERE dp.id = ?1")
    List<QuizAnswer> findAnswersByDeployedQuizId(Integer DeployedQuizId);
}
