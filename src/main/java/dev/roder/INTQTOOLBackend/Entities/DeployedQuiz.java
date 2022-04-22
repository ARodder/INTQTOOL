package dev.roder.INTQTOOLBackend.Entities;

import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class DeployedQuiz {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private Course deploymentCourse;
    @ManyToOne
    private Quiz deployedQuiz;
    private LocalDate deadline;
    @OneToMany
    private List<QuizAnswer> quizAnswer;

    public List<QuizAnswer> getQuizAnswer() {
        return quizAnswer;
    }

    public void setQuizAnswer(List<QuizAnswer> quizAnswer) {
        this.quizAnswer = quizAnswer;
    }

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

    public Quiz getDeployedQuiz() {
        return deployedQuiz;
    }

    public void setDeployedQuiz(Quiz depolyedQuiz) {
        this.deployedQuiz = depolyedQuiz;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getDetails(){
        JSONObject details = new JSONObject();
        details.put("id",id);
        details.put("courseId",deploymentCourse.getCourseID());
        details.put("deployedQuiz", deployedQuiz.getDetails());

        return details.toString();
    }
    public String toString(){
        JSONObject details = new JSONObject();
        details.put("id",id);
        details.put("courseId",deploymentCourse.getCourseID());
        details.put("deployedQuiz", deployedQuiz.toString());

        return details.toString();
    }

    public String getDetailsForEdit(){
        JSONObject details = new JSONObject();
        details.put("id",id);
        details.put("courseId",deploymentCourse.getCourseID());
        details.put("deployedQuiz", deployedQuiz.getEditDetails());

        return details.toString();
    }
}
