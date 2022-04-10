package dev.roder.INTQTOOLBackend.Entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String answer;
    private String status;
    private String feedback;
    private Integer grading;

    public Integer getGrading() {
        return grading;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        details.put("answer",this.answer);
        details.put("status",this.status);
        details.put("feedback",this.feedback);

        return details.toString();
    }
}
