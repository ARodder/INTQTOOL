package dev.roder.intqtoolbackend.Repositories;

import dev.roder.intqtoolbackend.Entities.Alternative;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the alternatives of questions.
 * Serves as interface between database and application.
 * Is auto-implemented into a Bean by Spring
 */
public interface AlternativeRepository extends CrudRepository<Alternative, Integer> {
}
