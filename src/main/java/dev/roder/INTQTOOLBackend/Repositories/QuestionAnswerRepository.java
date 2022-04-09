package dev.roder.INTQTOOLBackend.Repositories;

import dev.roder.INTQTOOLBackend.Entities.QuestionAnswer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuestionAnswerRepository extends CrudRepository<QuestionAnswer, Integer> {
}
