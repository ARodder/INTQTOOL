package dev.roder.intqtoolbackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class DeployedQuiz {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private Course deploymentCourse;
    @ManyToOne
    private Quiz quiz;
    private Timestamp deadline;
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

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz deployedQuiz) {
        this.quiz = deployedQuiz;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public String getQuestionAnswers(){
        JSONArray allQuizAnswers = new JSONArray();
        List<Integer> questionIds = quiz.getQuestionIds();
        int index = 1;
        for(Integer questionId: questionIds){
            JSONArray allQuestionAnswers = new JSONArray();
            for(QuizAnswer qa:quizAnswer){
                if(qa.getStatus().equals("submitted") || qa.getStatus().equals("graded")){
                    List<String> currentAnswerForQuestion = qa.getAnswerForQuestion(questionId);
                    for(String answerDetails:currentAnswerForQuestion){
                        if(answerDetails != null){
                            allQuestionAnswers.put(answerDetails);
                        }
                    }


                }

            }
            allQuizAnswers.put(allQuestionAnswers);
            index++;
        }
        return allQuizAnswers.toString();

    }

    public void checkAllAnswersGraded(){
        quizAnswer.forEach(qa -> qa.checkAllAnswersGraded());
    }

    public String getDetails(){
        JSONObject details = new JSONObject();
        details.put("id",id);
        details.put("courseId",deploymentCourse.getCourseID());
        details.put("quiz", quiz.getDetails());
        details.put("deadline", deadline.toString());

        return details.toString();
    }
    public String toString(){
        JSONObject details = new JSONObject();
        details.put("id",id);
        details.put("courseId",deploymentCourse.getCourseID());
        details.put("quiz", quiz.toString());
        String tempDeadline = deadline.toString();
        tempDeadline = tempDeadline.replace(" ","T");
        tempDeadline = tempDeadline +"Z";
        details.put("deadline", tempDeadline);

        return details.toString();
    }

    public String getDetailsForEdit(){
        JSONObject details = new JSONObject();
        details.put("id",id);
        details.put("courseId",deploymentCourse.getCourseID());
        details.put("quiz", quiz.getEditDetails());
        String tempDeadline = deadline.toString();
        tempDeadline = tempDeadline.replace(" ","T");
        tempDeadline = tempDeadline +"Z";
        details.put("deadline", tempDeadline);

        return details.toString();
    }
}
