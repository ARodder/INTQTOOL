package dev.roder.intqtoolbackend.Security;

import dev.roder.intqtoolbackend.Entities.Role;
import dev.roder.intqtoolbackend.Entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

/**
 * Class representing a user with springs required userDetails interface
 * needed for authentication
 */
public class IntqtoolUserDetails implements UserDetails {

    private String userName;
    private String password;
    private List<GrantedAuthority> authorities = new LinkedList<>();
    private boolean accountLock;
    private boolean userEnabled;
    private LocalDate expirationDate;

    /**
     * Constructor building userDetails from
     * user object from the database
     *
     * @param user user object from database
     */
    public IntqtoolUserDetails(User user){
        this.userName = user.getUsername();
        this.password = user.getPassword();
        this.accountLock = user.isAccountLock();
        this.userEnabled = user.isUserEnabled();
        this.expirationDate = user.getExpirationDate();
        this.convertRoles(user.getRoles());
    }

    /**
     * Converts a set of roles to granted Authorities
     * used when verifying what a user has access to.
     *
     * @param roles the roles to convert to granted authority
     */
    private void convertRoles(Set<Role> roles) {
        authorities.clear();
        for(Role tole:roles){
            authorities.add(new SimpleGrantedAuthority(tole.getName()));
        }
    }

    /**
     * Retrieves the granted authorities of the user
     *
     * @return returns granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Retrieves the encrypted password of the user
     *
     * @return Returns encrypted password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves a users username
     *
     * @return username of the user
     */
    @Override
    public String getUsername() {
        return userName;
    }

    /**
     * Determines if the user is expired
     *
     * @return Returns boolean depending on if the user is expired or not
     */
    @Override
    public boolean isAccountNonExpired() {
        boolean isExpired = false;
        if(this.expirationDate.compareTo(LocalDate.now())>0){
            isExpired = true;
        }
        return isExpired;
    }

    /**
     * Check if the account is locked
     *
     * @return Returns boolean depending on if the account is locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountLock;
    }

    /**
     * Checks if the credentials is expired
     *
     * @return Returns boolean depending on if the credentials has expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if the user is enabled
     *
     * @return Returns boolean depending on if the user is enabled
     */
    @Override
    public boolean isEnabled() {
        return userEnabled;
    }
}