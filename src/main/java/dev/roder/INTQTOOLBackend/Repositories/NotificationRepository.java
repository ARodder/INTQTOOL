package dev.roder.INTQTOOLBackend.Repositories;

import org.springframework.data.repository.CrudRepository;

import dev.roder.INTQTOOLBackend.Entities.Notification;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface NotificationRepository extends CrudRepository<Notification, Integer> {

}