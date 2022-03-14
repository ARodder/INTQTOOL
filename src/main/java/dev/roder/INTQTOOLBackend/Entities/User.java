package dev.roder.INTQTOOLBackend.Entities;

import java.sql.Date;
import java.util.List;
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
    private String userName;
    private String email;
    private String roles;
    private Date expirationDate;
    private boolean accountLock;
    private boolean userEnabled;


    public User(){

    }

    public Integer getUserID () {
        return id;
    }
    public void setUserID (Integer id) {
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
    public String getUserName () {
        return userName;
    }
    public void setUserName (String userName) {
        this.userName=userName;
    }
    public boolean isValid(){
        return true;
    }
    public String getRoles() {return roles;}
    public void setRoles(String roles) {this.roles = roles;}
    public Date getExpirationDate() {return expirationDate;}
    public void setExpirationDate(Date expirationDate) {this.expirationDate = expirationDate;}
    public boolean isAccountLock() {return !accountLock;}
    public void setAccountLock(boolean accountLock) {this.accountLock = accountLock;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public boolean isUserEnabled() {return userEnabled;}
    public void setUserEnabled(boolean userEnabled) {this.userEnabled = userEnabled;}

}
