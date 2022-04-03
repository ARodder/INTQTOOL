package dev.roder.INTQTOOLBackend.Entities;

import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Course {

    @Id
    @Unique
    private String courseID;

    private String courseName;

    private String description;

    @Column(unique = true)
    private String joinCode;
    @OneToMany
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


}
