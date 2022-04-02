package dev.roder.INTQTOOLBackend.Entities;

import dev.roder.INTQTOOLBackend.Security.Authorities.IntqtoolUserRole;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String password;
    private String settings;
    @OneToMany
    private List<Course> courses;
    @Unique
    private String username;
    @Unique
    private String email;
    private IntqtoolUserRole role;
    private String firstName;
    private String lastName;
    private LocalDate expirationDate;
    private boolean accountLock;
    private boolean userEnabled;


    public User(){

    }

    public Integer getId () {
        return id;
    }
    public void setId (Integer id) {
        this.id=id;
    }


    public String getPassword () {
        return password;
    }
    public void setPassword (String passHash) {
        this.password=passHash;
    }
    public String getSettings () {
        return settings;
    }
    public void setSettings (String settings) {
        this.settings=settings;
    }
    public List<Course> getCourses () {
        return courses;
    }
    public void setCourses (List<Course> courses) {
        this.courses=courses;
    }
    public String getUsername() {
        return username;
    }


    public void setUsername(String userName) {
        this.username=userName;
    }
    public boolean isValid(){
        return true;
    }
    public IntqtoolUserRole getRole() {return role;}
    public void setRole(IntqtoolUserRole roles) {this.role = roles;}
    public LocalDate getExpirationDate() {return expirationDate;}
    public void setExpirationDate(LocalDate expirationDate) {this.expirationDate = expirationDate;}
    public boolean isAccountLock() {return !accountLock;}
    public void setAccountLock(boolean accountLock) {this.accountLock = accountLock;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public boolean isUserEnabled() {return userEnabled;}
    public void setUserEnabled(boolean userEnabled) {this.userEnabled = userEnabled;}
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    @Override
    public String toString() {
        StringBuilder userString = new StringBuilder();
        userString.append("{\n");
        userString.append("\"id\":"+this.id+",");
        userString.append("\"username\":"+"\""+this.username+"\"," );
        userString.append("\"courses\":"+"[");
        courses.forEach(course -> {userString.append(course.getCourseID()+",");});
        if(courses.size() >=1){
            userString.deleteCharAt(userString.length()-1);
        }
        userString.append("],");
        userString.append("\"email\":"+"\""+this.email+"\"," );
        userString.append("\"roles\":"+"\""+this.role +"\"," );
        userString.append("}");
        return userString.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities
                = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role.name()));
            role.getPermissions().stream()
                    .map(p -> new SimpleGrantedAuthority(p.name()))
                    .forEach(authorities::add);

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        boolean isExpired = false;
        if(this.expirationDate.compareTo(LocalDate.now())>0){
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
