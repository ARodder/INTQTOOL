package dev.roder.intqtoolbackend.Repositories;

import org.springframework.data.repository.CrudRepository;

import dev.roder.intqtoolbackend.Entities.Notification;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface NotificationRepository extends CrudRepository<Notification, Integer> {
    Optional<Notification> findByNotificationID(Integer Notification);
}