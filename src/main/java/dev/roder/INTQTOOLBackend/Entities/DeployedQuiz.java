package dev.roder.INTQTOOLBackend.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class DeployedQuiz {

    @Id
    private Integer id;
    @ManyToOne
    private Course deploymentCourse;
    @ManyToOne
    private Quiz depolyedQuiz;
    private LocalDate deadline;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Course getDeploymentCourse() {
        return deploymentCourse;
    }

    public void setDeploymentCourse(Course deploymentCourse) {
        this.deploymentCourse = deploymentCourse;
    }

    public Quiz getDepolyedQuiz() {
        return depolyedQuiz;
    }

    public void setDepolyedQuiz(Quiz depolyedQuiz) {
        this.depolyedQuiz = depolyedQuiz;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
