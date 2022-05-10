package dev.roder.intqtoolbackend.Repositories;

import org.springframework.data.repository.CrudRepository;

import dev.roder.intqtoolbackend.Entities.Course;

import java.util.Optional;

/**
 * Repository for the courses.
 * Serves as interface between database and application.
 * Is auto-implemented into a Bean by Spring
 */
public interface CourseRepository extends CrudRepository<Course, Integer> {

    /**
     * Finds a course based on the randomly generated joinCode
     *
     * @param joinCode randomly generated joinCode
     * @return Returns optional containing the course if found.
     */
    Optional<Course> findByJoinCode(String joinCode);
}