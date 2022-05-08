package dev.roder.intqtoolbackend.RequestObject;

import java.util.List;

/**
 * Class that represent an object received in the body of a requests
 * to retrieve all the fields.
 */
public class GradeAnswerRequest {

    private Double grade;
    private String feedback;
    private Integer deployedQuizId;
    private List<Integer> answerIds;

    /**
     * Retrieves the deployedQuizId which corresponds
     * to the id of the deployment the answerIds belong to
     *
     * @return Returns deployedQuizId
     */
    public Integer getDeployedQuizId() {
        return deployedQuizId;
    }

    /**
     * Sets the deployedQuizId to a new value
     *
     * @param deployedQuizId new value for the deployedQuizId
     */
    public void setDeployedQuizId(Integer deployedQuizId) {
        this.deployedQuizId = deployedQuizId;
    }

    /**
     * Retrieves the grade to give to all the answers in the answerIds
     *
     * @return Returns the grade
     */
    public Double getGrade() {
        return grade;
    }

    /**
     * Sets the grade to a new value
     *
     * @param grade new value for the grade
     */
    public void setGrade(Double grade) {
        this.grade = grade;
    }

    /**
     * Retrieves the feedback to give to each of the
     * answers in the answerIds list
     *
     * @return Returns the feedback to give
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * Sets the feedback to a new value
     *
     * @param feedback new value for the feedback
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * Retrieves the list of answers to grade with the given details(grade, feedback)
     *
     * @return List of answerIds
     */
    public List<Integer> getAnswerIds() {
        return answerIds;
    }

    /**
     * Sets the list of answerIds to a new value
     *
     * @param answerIds new value for the answerId List.
     */
    public void setAnswerIds(List<Integer> answerIds) {
        this.answerIds = answerIds;
    }
}
