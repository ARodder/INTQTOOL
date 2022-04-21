package dev.roder.INTQTOOLBackend.Entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Entity
public class DeployedQuiz {

    @Id
    private Integer id;
    @ManyToOne
    private Course deploymentCourse;
    @ManyToOne
    private Quiz depolyedQuiz;
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

    public String getDetails(){
        JSONObject details = new JSONObject();
        details.put("id",id);
        details.put("quiz",depolyedQuiz.getDetails());

        return details.toString();
    }
    public String toString(){
        return depolyedQuiz.toString();
    }
}
