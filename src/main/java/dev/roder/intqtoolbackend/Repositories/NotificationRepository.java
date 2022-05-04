package dev.roder.intqtoolbackend.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import dev.roder.intqtoolbackend.Entities.Notification;

import java.util.List;
import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface NotificationRepository extends CrudRepository<Notification, Integer> {
    Optional<Notification> findByNotificationID(Integer Notification);

    @Query(value="SELECT u.notifications FROM User u WHERE u.id = ?1")
    List<Notification> findNotificationsByUserId(Integer userId);
}