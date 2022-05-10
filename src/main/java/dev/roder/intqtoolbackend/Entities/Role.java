package dev.roder.intqtoolbackend.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Entity class representing a role from the database.
 * Used to verify the role of a user.
 */
@Entity(name="roles")
public class Role {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new LinkedHashSet<>();

    /**
     * Empty constructor needed for JPA
     */
    public Role() {
    }

    /**
     * Constructor for role taking single parameter.
     *
     * @param name name of the role(requires prefix "ROLE_" for spring security
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     * Retrieves the id of the role
     *
     * @return Returns the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id to a new value
     *
     * @param id new value of the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves users with the given role.
     *
     * @return Returns set of users
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Sets the users Set to a new value
     * @param users new value of the user Set
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /**
     * Retrieves the name of the role
     *
     * @return Returns name of the role
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of the role to a new value
     *
     * @param name new value for the name of the role
     */
    public void setName(String name) {
        this.name = name;
    }
}
