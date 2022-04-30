package dev.roder.intqtoolbackend.Repositories;

import org.springframework.data.repository.CrudRepository;

import dev.roder.intqtoolbackend.Entities.Question;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface QuestionRepository extends CrudRepository<Question, Integer> {

}