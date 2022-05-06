package dev.roder.intqtoolbackend.Entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer notificationID;
    private String title;
    private String quizID;
    private String type;
    private Integer quizAnswerId;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public Integer getNotificationID () {
        return notificationID;
    }

    public void setNotificationID (Integer notificationID) {
        this.notificationID=notificationID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuizID () {
        return quizID;
    }

    public void setQuizID (String quizID) {
        this.quizID=quizID;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type=type;
    }

    public Integer getQuizAnswerId() {
        return quizAnswerId;
    }

    public void setQuizAnswerId(Integer quizAnswerId) {
        this.quizAnswerId = quizAnswerId;
    }


    public String getDetails(){
        JSONObject details = new JSONObject();
        details.put("id",notificationID);
        details.put("title",this.title);
        details.put("message",this.message);
        details.put("type",this.type);
        details.put("quizAnswerId",this.quizAnswerId);

        return details.toString();
    }
}
