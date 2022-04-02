package dev.roder.INTQTOOLBackend.Entities;

import org.checkerframework.common.aliasing.qual.Unique;

import java.time.LocalDate;
import java.util.*;
import javax.persistence.*;

@Entity
public class User{

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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles = new LinkedHashSet<>();
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
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void addRole(Role role) {roles.add(role);}
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
        userString.append("\"roles\":"+"[" );
        roles.forEach(role ->{userString.append("\""+role.getName()+"\",");});
        if(roles.size() >=1){
            userString.deleteCharAt(userString.length()-1);
        }
        userString.append("]");
        userString.append("}");
        return userString.toString();
    }


}
