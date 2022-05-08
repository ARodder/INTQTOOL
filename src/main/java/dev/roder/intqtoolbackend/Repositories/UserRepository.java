package dev.roder.intqtoolbackend.Repositories;

import org.springframework.data.repository.CrudRepository;

import dev.roder.intqtoolbackend.Entities.User;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

/**
 * Repository for the Users.
 * Serves as interface between database and application.
 * Is auto-implemented into a Bean by Spring
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * Retrieves a user based on a given username.
     *
     * @param username username of the user to find.
     * @return Returns optional containing the user if it exists in the database.
     */
    Optional<User> findByUsername(String username);
}