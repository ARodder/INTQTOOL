package dev.roder.intqtoolbackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * Entity class representing a deployment of a quiz stored in the database as DeploymentQuiz,
 * allowing a quiz to be deployed multiple times with independent lists of answers and deadline.
 */
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

    /**
     * Retrieves the list of quizAnswers in the deployedQuiz.
     *
     * @return Returns list of quizAnswers
     */
    public List<QuizAnswer> getQuizAnswer() {
        return quizAnswer;
    }

    /**
     * Sets the list of quizAnswers to a new value
     *
     * @param quizAnswer new value to set the quizAnswer list to.
     */
    public void setQuizAnswer(List<QuizAnswer> quizAnswer) {
        this.quizAnswer = quizAnswer;
    }

    /**
     * Retrieves the id of the deployedQuiz
     *
     * @return Returns the id of the deployedQuiz.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the deployedQuiz to a new value
     *
     * @param id new value to set the deployedQuiz id to.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Adds a new quizAnswer to the deployedQuiz
     *
     * @param qa new quizAnswer to add
     */
    public void addQuizAnswer(QuizAnswer qa){
        quizAnswer.add(qa);
    }

    /**
     * Retrieves the course the quiz is deployed to.
     *
     * @return Returns the course the quiz is deployed to.
     */
    public Course getDeploymentCourse() {
        return deploymentCourse;
    }

    /**
     * Sets the deploymentCourse to a new value.
     *
     * @param deploymentCourse the new value to set the deploymentCourse to.
     */
    public void setDeploymentCourse(Course deploymentCourse) {
        this.deploymentCourse = deploymentCourse;
    }

    /**
     * Retrieves the quiz in the deploymentQuiz
     *
     * @return Returns the quiz in the deploymentQuiz.
     */
    public Quiz getQuiz() {
        return quiz;
    }

    /**
     * Sets the quiz of the deployedQuiz to a new value
     *
     * @param quiz new value to set the quiz to.
     */
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    /**
     * Retrieves the deadline of the deploydQuiz.
     *
     * @return Returns the deadline of the deployedQuiz
     */
    public Timestamp getDeadline() {
        return deadline;
    }

    /**
     * Sets the deadline of the deployedQuiz to a new value
     *
     * @param deadline new value to set the deadline to.
     */
    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    /**
     * Retrieves a JSONArray containing all answers that is either submitted or graded in the deployedQuiz in the form of a string .
     * Answers are sorted in to array based on their corresponding questions.
     *
     * @return Returns the string version of the JSONArray containing the answers.
     */
    public String getQuestionAnswers(){
        JSONArray allQuizAnswers = new JSONArray();
        List<Integer> questionIds = quiz.getQuestionIds();

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

        }
        return allQuizAnswers.toString();

    }


    /**
     * Builds a JSONObject containing the id, courseId, quiz details and deadline of the deployedQuiz.
     *
     * @return Returns String version of the JSONObject.
     */
    public String getDetails(){
        JSONObject details = new JSONObject();
        details.put("id",id);
        details.put("courseId",deploymentCourse.getCourseID());
        details.put("quiz", quiz.getDetails());
        details.put("deadline", deadline.toString());

        return details.toString();
    }
    /**
     * Builds a JSONObject containing the id, courseId, quiz toString details and deadline of the deployedQuiz.
     *
     * @return Returns String version of the JSONObject.
     */
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

    /**
     * Builds a JSONObject containing the id, courseId, quiz editDetails and deadline of the deployedQuiz.
     *
     * @return Returns String version of the JSONObject.
     */
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
