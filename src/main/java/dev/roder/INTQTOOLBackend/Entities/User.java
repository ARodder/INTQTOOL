package dev.roder.INTQTOOLBackend.Entities;

import java.util.List;
import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;

    private String passHash;

    private String settings;

    @OneToMany
    private List<Course> courses;

    private String userName;

    public User(){

    }

    public String getUserID () {
        return id;
    }

    public void setUserID (String id) {
        this.id=id;
    }

    public String getPassHash () {
        return passHash;
    }

    public void setPassHash (String passHash) {
        this.passHash=passHash;
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




}
