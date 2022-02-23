package dev.roder.INTQTOOLBackend.Repositories;

import org.springframework.data.repository.CrudRepository;

import dev.roder.INTQTOOLBackend.Entities.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {

}