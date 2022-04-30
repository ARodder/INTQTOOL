package dev.roder.intqtoolbackend.RequestObject;

import java.util.List;

public class GradeAnswerRequest {

    private Double grade;
    private String feedback;
    private Integer deployedQuizId;
    private List<Integer> answerIds;

    public Integer getDeployedQuizId() {
        return deployedQuizId;
    }

    public void setDeployedQuizId(Integer deployedQuizId) {
        this.deployedQuizId = deployedQuizId;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public List<Integer> getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(List<Integer> answerIds) {
        this.answerIds = answerIds;
    }
}
