package dev.roder.intqtoolbackend.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import dev.roder.intqtoolbackend.Entities.Notification;

import java.util.List;
import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

/**
 * Repository for the notifications of users.
 * Serves as interface between database and application.
 * Is auto-implemented into a Bean by Spring.
 */
public interface NotificationRepository extends CrudRepository<Notification, Integer> {
    /**
     * Retrieves a specific notification based on the notification id.
     *
     * @param Notification id of the notification to retrieve.
     * @return Returns an optional containing the notification if found.
     */
    Optional<Notification> findByNotificationID(Integer Notification);

    /**
     * Retrieves all notifications of a user.
     * used when retrieving notifications outside a normal request
     * session(used by websockets).
     *
     * @param userId id of the user to retrieve the notifications from.
     * @return Returns a list of the found notifications.
     */
    @Query(value="SELECT u.notifications FROM User u WHERE u.id = ?1")
    List<Notification> findNotificationsByUserId(Integer userId);
}