package dev.roder.INTQTOOLBackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private Date deadline;
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

    public void addQuizAnswer(QuizAnswer qa){
        quizAnswer.add(qa);
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getQuestionAnswers(){
        JSONArray allQuizAnswers = new JSONArray();
        List<Integer> questionIds = deployedQuiz.getQuestionIds();
        for(Integer questionId: questionIds){
            JSONArray allQuestionAnswers = new JSONArray();
            for(QuizAnswer qa:quizAnswer){
                if(qa.getStatus().equals("submitted"))
                allQuestionAnswers.put(qa.getAnswerForQuestion(questionId));
            }
            allQuizAnswers.put(allQuestionAnswers);
        }
        return allQuizAnswers.toString();

    }

    public String getDetails(){
        JSONObject details = new JSONObject();
        details.put("id",id);
        details.put("courseId",deploymentCourse.getCourseID());
        details.put("deployedQuiz", deployedQuiz.getDetails());
        details.put("deadline", deadline.toString());

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
