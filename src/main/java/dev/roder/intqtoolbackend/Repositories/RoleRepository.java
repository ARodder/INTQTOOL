package dev.roder.intqtoolbackend.Repositories;

import dev.roder.intqtoolbackend.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for the Roles of Users.
 * Serves as interface between database and application.
 * Is auto-implemented into a Bean by Spring
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
}
