package dev.roder.INTQTOOLBackend.Entities;

import org.checkerframework.common.aliasing.qual.Unique;

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
    @Unique
    private String username;
    @Unique
    private String email;
    private String roles;
    private String firstName;
    private String lastname;
    private Date expirationDate;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
        userString.append("\"roles\":"+"\""+this.roles+"\"," );
        userString.append("}");
        return userString.toString();
    }
}
