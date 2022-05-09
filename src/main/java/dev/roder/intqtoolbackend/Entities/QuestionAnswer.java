package dev.roder.intqtoolbackend.Entities;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Entity class representing a answer to a single question, and is stored in the database.
 * QuestionAnswer contains id, answer to a question, questionId id of the question the answer is for,
 * status, feedback and grading.
 */
@Entity
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 500)
    private String answer;
    private Integer questionId;
    private String status;
    @Column(length = 500)
    private String feedback;
    private double grading;

    /**
     * Retrieves the grading of the question
     *
     * @return Returns the grading of the answer.
     */
    public double getGrading() {
        return grading;
    }

    /**
     * Retrieves the questionId of the answer
     *
     * @return Returns the questionId
     */
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     * Sets the questionId to a new value
     *
     * @param questionId new value of the questionId
     */
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    /**
     * Sets the grading to a new value.
     * makes sure the grading is set to either 0, 0.5, 1.
     *
     * @param grading new value of the grading.
     */
    public void setGrading(double grading) {
        if (grading >= 1) {
            this.grading = 1;
        } else if (grading <= 0) {
            this.grading = 0;
        } else {
            this.grading = grading;
        }
    }

    /**
     * Retrieves the id of the question answer
     *
     * @return id of the question answer.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the question id to a new value
     *
     * @param id new value for the id.
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Retrieves the answer of the questionAnswer
     *
     * @return Returns the answer to the question
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Sets the answer of the questionAnswer to a new value.
     *
     * @param answer new value of the answer.
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Retrieves the status of the questionAnswer, can be (in-progress,submitted or graded)
     *
     * @return Returns the status of the questionAnswer.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the questionAnswer to a new value
     *
     * @param status new value of the status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieves the feedback of the questionAnswer
     *
     * @return Returns the feedback for the questionAnswer.
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * Sets the feedback to a new value
     *
     * @param feedback new value for the feedback.
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * Builds a JSONObject containing certain details of the questionAnswer
     * Contains id, questionId, answer, status and feedback.
     *
     * @return Returns a string version of the JSONObject
     */
    public String toString() {
        JSONObject details = new JSONObject();
        details.put("id", this.id);
        details.put("questionId", this.questionId);
        details.put("answer", this.answer);
        details.put("status", this.status);
        details.put("feedback", this.feedback);

        return details.toString();
    }

    /**
     * Builds a JSONObject containing certain details of the questionAnswer.
     * Contains id, userId, questionId, answer, status, grading and feedback.
     *
     * @param userId id of the user that answered the question.
     * @return Returns a string version of the JSONObject.
     */
    public String getGradingDetails(Integer userId) {
        JSONObject details = new JSONObject();
        details.put("id", this.id);
        details.put("userId", userId);
        details.put("questionId", this.questionId);
        details.put("answer", this.answer);
        details.put("status", this.status);
        details.put("grading", this.grading);
        details.put("feedback", this.feedback);

        return details.toString();

    }
}
