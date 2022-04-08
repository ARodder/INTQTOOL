package dev.roder.INTQTOOLBackend.Entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class QuestionAnswer {

    @Id
    private int id;
    private String userID;
    private String courseID;
    private String answer;
    private String status;
    private String feedback;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String toString(){
        JSONObject details = new JSONObject();
        details.put("id",this.id);
        details.put("userId",this.userID);
        details.put("courseId",this.courseID);
        details.put("answer",this.answer);
        details.put("status",this.status);
        details.put("feedback",this.feedback);

        return details.toString();
    }
}
