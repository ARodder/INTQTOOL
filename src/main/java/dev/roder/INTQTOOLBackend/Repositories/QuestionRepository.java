package dev.roder.INTQTOOLBackend.Repositories;

import dev.roder.INTQTOOLBackend.Entities.QuestionAnswer;
import dev.roder.INTQTOOLBackend.Entities.Quiz;
import org.springframework.data.repository.CrudRepository;

import dev.roder.INTQTOOLBackend.Entities.Question;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface QuestionRepository extends CrudRepository<Question, Integer> {

}