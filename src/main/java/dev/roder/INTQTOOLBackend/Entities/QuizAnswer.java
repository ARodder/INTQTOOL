package dev.roder.INTQTOOLBackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class QuizAnswer {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private DeployedQuiz deployedQuiz;
    @ManyToOne
    private User user;
    private String status;
    private Integer grading;
    private Timestamp submittedDate;
    @ManyToMany
    @JoinTable(name="quiz_answer_questions",
            joinColumns = @JoinColumn(name="quiz_answer_id"),
            inverseJoinColumns = @JoinColumn(name="question_answer_id")
    )
    private List<QuestionAnswer> answers;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DeployedQuiz getDeployedQuiz() {
        return deployedQuiz;
    }

    public void setDeployedQuiz(DeployedQuiz deployedQuiz) {
        this.deployedQuiz = deployedQuiz;
    }

    public Integer getGrading() {
        return grading;
    }

    public void setGrading(Integer grading) {
        this.grading = grading;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Timestamp submittedDate) {
        this.submittedDate = submittedDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswerForQuestion(Integer questionId){
        List<QuestionAnswer> answer = answers.stream().filter((ans)->ans.getQuestionId()==questionId).collect(Collectors.toList());
        if(answer.size() == 1){
            return answer.get(0).getGradingDetails(user.getId());
        }else{
            return null;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<QuestionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionAnswer> answers) {
        this.answers = answers;
    }

//    public String getDetails(){
//        JSONObject details = new JSONObject();
//        details.put("id",this.id);
//        details.put("userId",this.user.getId());
//        details.put("courseId",this.courseID);
//        details.put("status",this.status);
//        details.put("quizId",this.quizId);
//
//        return details.toString();
//    }

    @Override
    public String toString(){
        JSONObject details = new JSONObject();
        details.put("id",this.id);
        details.put("userId",this.user.getId());
        details.put("courseId",this.deployedQuiz.getDeploymentCourse().getCourseID());
        details.put("status",this.status);
        details.put("quizId",this.deployedQuiz.getDeployedQuiz().getQuizID());
        JSONArray answerArray = new JSONArray();
        for(QuestionAnswer answer: answers){
            answerArray.put(answer.toString());
        }
        details.put("answers",answerArray);

        return details.toString();
    }
}
