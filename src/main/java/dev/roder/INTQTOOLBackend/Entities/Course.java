package dev.roder.INTQTOOLBackend.Entities;


import org.json.JSONObject;

import javax.persistence.*;
import java.util.*;

@Entity
public class Course {

    @Id
    private String courseID;

    private String courseName;

    private String description;



    @ManyToMany(mappedBy="courses")
    private Set<User> users = new LinkedHashSet<>();

    @Column(unique = true)
    private String joinCode;
    @OneToMany
    @JoinTable(name="course_active_quizzes",
            joinColumns = @JoinColumn(name="course_id"),
            inverseJoinColumns = @JoinColumn(name="quiz_id")
    )
    private List<Quiz> activeQuizzes;

    public String getCourseID () {
        return courseID;
    }

    public void setCourseID (String courseID) {
        this.courseID=courseID;
    }

    public String getCourseName () {
        return courseName;
    }

    public void setCourseName (String courseName) {
        this.courseName=courseName;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description=description;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public List<Quiz> getActiveQuizzes() {
        return activeQuizzes;
    }

    public void setActiveQuizzes(List<Quiz> activeQuizzes) {
        this.activeQuizzes = activeQuizzes;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
    public void addUser(User user){
        this.users.add(user);
    }

    public String getDetails(){
        JSONObject details = new JSONObject();
        details.put("id",this.courseID);
        details.put("name",this.courseName);
        details.put("description",description);
        details.put("joinCode",this.joinCode);
        details.put("activeQuizAmnt",this.activeQuizzes.size());

        return details.toString();
    }


}
