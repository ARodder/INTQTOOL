package dev.roder.INTQTOOLBackend.Entities;

import org.checkerframework.common.aliasing.qual.Unique;
import org.json.JSONArray;
import org.json.JSONObject;

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

    @OneToMany
    private List<Notification> notifications;
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

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void addNotification(Notification notification){
        this.notifications.add(notification);
    }

    @Override
    public String toString() {

        JSONObject userObject = new JSONObject();
        userObject.put("id",id);
        userObject.put("username",username);
        userObject.put("firstName",firstName);
        userObject.put("lastName",lastName);
        JSONArray courseJsonArray = new JSONArray();
        for(Course course:courses){
            courseJsonArray.put(course.getCourseID());
        }
        userObject.put("courses",courseJsonArray);
        userObject.put("email",email);

        JSONArray roleJsonArray = new JSONArray();
        for(Role role:roles){
            roleJsonArray.put(role.getName());
        }
        userObject.put("roles",roleJsonArray);
        JSONArray notificationJsonArray = new JSONArray();
        for(Notification notification:notifications){
            notificationJsonArray.put(notification.getDetails());
        }
        userObject.put("notifications",notificationJsonArray);

        return userObject.toString();
    }


}
