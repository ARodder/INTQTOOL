package dev.roder.intqtoolbackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity class representing a quizAnswer stored in the database.
 * Contains id, deployedQuiz, user, status of the entire quiz,
 * grading of the entire quiz, notified and submittedDate.
 */
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
    private Double grading;
    private boolean notified;
    private Timestamp submittedDate;
    @ManyToMany
    @JoinTable(name="quiz_answer_questions",
            joinColumns = @JoinColumn(name="quiz_answer_id"),
            inverseJoinColumns = @JoinColumn(name="question_answer_id")
    )
    private List<QuestionAnswer> answers;

    /**
     * Retrieves the user of the quizAnswer
     *
     * @return Returns the user of the answer
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user to a new value.
     *
     * @param user new value for the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retrieves the deployedQuiz that the answer is for
     *
     * @return Returns the deployedQuiz.
     */
    public DeployedQuiz getDeployedQuiz() {
        return deployedQuiz;
    }

    /**
     * Sets the deployedQuiz to a new value.
     *
     * @param deployedQuiz new value for the deployedQuiz
     */
    public void setDeployedQuiz(DeployedQuiz deployedQuiz) {
        this.deployedQuiz = deployedQuiz;
    }

    /**
     * Retrieves the grading of the entire quiz
     * (sum of the grading of all questionAnswers in the quizAnswer)
     *
     * @return Returns the grading of the quiz.
     */
    public Double getGrading() {
        return grading;
    }

    /**
     * Sets the grading of the quizAnswer to a new value.
     *
     * @param grading new value for the quizAnswer grading.
     */
    public void setGrading(Double grading) {
        this.grading = grading;
    }

    /**
     * Retrieves the status of the quizAnswer.
     *
     * @return Returns the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Retrieves the date the quizAnswer was submitted.
     *
     * @return Returns the date the quizAnswer was submitted
     */
    public Timestamp getSubmittedDate() {
        return submittedDate;
    }

    /**
     * Sets the date the quizAnswer was submitted to a new date.
     *
     * @param submittedDate new value for the submitted date
     */
    public void setSubmittedDate(Timestamp submittedDate) {
        this.submittedDate = submittedDate;
    }

    /**
     * Sets the status of the quizAnswer to a new value.
     *
     * @param status new value for the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieves the answers to a specific question based on the id of the question.
     *
     * @param questionId id of the question to find the answers for
     * @return returns list of questionAnswers to the corresponding question.
     */
    public List<String> getAnswerForQuestion(Integer questionId){
        List<QuestionAnswer> answer = answers.stream().filter((ans)->ans.getQuestionId()==questionId).collect(Collectors.toList());
        if(answer.size() >= 1){
            return answer.stream().map((ans)->ans.getGradingDetails(user.getId())).collect(Collectors.toList());
        }else{
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves the id of the quizAnswer.
     *
     * @return Returns the id of the quizAnswer
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the quizAnswer to a new value.
     *
     * @param id new value for the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retrieves the list of questionAnswers in the quizAnswer,
     *
     * @return Returns the list of questionAnswers.
     */
    public List<QuestionAnswer> getAnswers() {
        return answers;
    }

    /**
     * Sets the answers of the questionAnswer list to a new value
     * @param answers new value for the questionAnswer list.
     */
    public void setAnswers(List<QuestionAnswer> answers) {
        this.answers = answers;
    }

    /**
     * Checks if all questionAnswers are graded, if they all are graded
     * the status is set to graded and the sum of the grading of the
     * questionAnswers is set as the value of the grading in the quizAnswer
     *
     * @return Returns boolean based on if the quizAnswer was graded.
     */
    public boolean checkAllAnswersGraded(){
        boolean allAnswersGraded = true;
        Double tempGrade = 0.0;
        for(QuestionAnswer ans:answers){
            if(!ans.getStatus().equals("graded")){
                allAnswersGraded = false;
                break;
            }else{
                tempGrade = tempGrade + ans.getGrading();
            }
        }

        if(allAnswersGraded){
            setStatus("graded");
            setGrading(tempGrade);
        }

        return allAnswersGraded;

    }

    /**
     * Retrieves weather or not a notification has been created for the quizAnswer.
     *
     * @return Returns boolean true if a notification was already created for the quizAnswer.
     */
    public boolean isNotified() {
        return notified;
    }

    /**
     * Sets the notified to a new value
     *
     * @param notified new value to set the notified to.
     */
    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    /**
     * Builds JSONObject containing details of the QuizAnswer
     * Contains id,userId,courseId,status,quizId and an array of the questionAnswer using toString()
     *
     * @return Returns a string version of the JSONObject.
     */
    @Override
    public String toString(){
        JSONObject details = new JSONObject();
        details.put("id",this.id);
        details.put("userId",this.user.getId());
        details.put("courseId",this.deployedQuiz.getDeploymentCourse().getCourseID());
        details.put("status",this.status);
        details.put("deployedQuizId",this.deployedQuiz.getId());
        JSONArray answerArray = new JSONArray();
        for(QuestionAnswer answer: answers){
            answerArray.put(answer.toString());
        }
        details.put("answers",answerArray);

        return details.toString();
    }

    /**
     * Builds JSONObject containing details of the QuizAnswer
     * Contains id,userId,courseId,status,quizId and an array of the questionAnswer with gradingDetails
     *
     * @return Returns a string version of the JSONObject.
     */
    public String getAnswerDetails(){
        JSONObject details = new JSONObject();
        details.put("id",this.id);
        details.put("userId",this.user.getId());
        details.put("courseId",this.deployedQuiz.getDeploymentCourse().getCourseID());
        details.put("status",this.status);
        details.put("deployedQuizId",this.deployedQuiz.getId());
        JSONArray answerArray = new JSONArray();
        for(QuestionAnswer answer: answers){
            answerArray.put(answer.getGradingDetails(user.getId()));
        }
        details.put("answers",answerArray);

        return details.toString();
    }


}
