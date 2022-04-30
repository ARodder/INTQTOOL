package dev.roder.intqtoolbackend.Repositories;

import dev.roder.intqtoolbackend.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
