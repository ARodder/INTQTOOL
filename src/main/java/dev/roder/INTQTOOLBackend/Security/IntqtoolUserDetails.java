package dev.roder.INTQTOOLBackend.Security;

import dev.roder.INTQTOOLBackend.Entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class IntqtoolUserDetails implements UserDetails {

    private String userName;
    private String password;
    private List<GrantedAuthority> authorities;
    private boolean accountLock;
    private boolean userEnabled;
    private Date expirationDate;

    public IntqtoolUserDetails(User user){
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.authorities = Arrays.stream(user.getRoles().split(","))
                .map(SimpleGrantedAuthority :: new)
                .collect(Collectors.toList());
        this.accountLock = user.isAccountLock();
        this.userEnabled = user.isUserEnabled();
        this.expirationDate = user.getExpirationDate();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        boolean isExpired = false;
        if(this.expirationDate.compareTo(Date.valueOf(LocalDate.now()))>0){
            isExpired = true;
        }
        return isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountLock;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userEnabled;
    }
}
