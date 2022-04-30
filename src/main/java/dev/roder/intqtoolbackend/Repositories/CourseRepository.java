package dev.roder.intqtoolbackend.Repositories;

import org.springframework.data.repository.CrudRepository;

import dev.roder.intqtoolbackend.Entities.Course;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface CourseRepository extends CrudRepository<Course, Integer> {
    Optional<Course> findByJoinCode(String joinCode);
}