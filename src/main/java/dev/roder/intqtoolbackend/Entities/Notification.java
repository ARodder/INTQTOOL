package dev.roder.intqtoolbackend.Entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity class representing a notification from the database.
 * Notifications contain notificationId, title, quizId, type,
 * quizAnswerId and message.
 */
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer notificationID;
    private String title;
    private String quizID;
    private String type;
    private Integer quizAnswerId;
    private String message;

    /**
     * Retrieves the message of the notification.
     *
     * @return Returns the message of the notification
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of the notification to a new value.
     *
     * @param message new value to set the message of the notification to.
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * Retrieves the id of the notification.
     *
     * @return Returns the id of the notification.
     */
    public Integer getNotificationID() {
        return notificationID;
    }

    /**
     * Sets the id of the notification to the new value
     *
     * @param notificationID New value to set the notificationID to.
     */
    public void setNotificationID(Integer notificationID) {
        this.notificationID = notificationID;
    }

    /**
     * Retrieves the title of the notification
     *
     * @return Returns the title of the notification
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title to a new value
     *
     * @param title new value to set the title to
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the id of the quiz the notification is for.
     *
     * @return Returns the quizId.
     */
    public String getQuizID() {
        return quizID;
    }

    /**
     * Sets the quizId to a new value
     *
     * @param quizID new value to set the quizId to.
     */
    public void setQuizID(String quizID) {
        this.quizID = quizID;
    }

    /**
     * Retrieves the type of notification.
     *
     * @return Returns the type of notification
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the notification to a new value
     *
     * @param type new value to set the notification type to.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the id of the answer the notification is for.
     *
     * @return id of the quizAnswer
     */
    public Integer getQuizAnswerId() {
        return quizAnswerId;
    }

    /**
     * Sets the quizAnswerId to a new value
     *
     * @param quizAnswerId new value to set the quizAnswerId to.
     */
    public void setQuizAnswerId(Integer quizAnswerId) {
        this.quizAnswerId = quizAnswerId;
    }


    /**
     * Builds a JSONObject containing details of the notification.
     *
     * @return Returns a string version of the JSONObject.
     */
    public String getDetails() {
        JSONObject details = new JSONObject();
        details.put("id", notificationID);
        details.put("title", this.title);
        details.put("message", this.message);
        details.put("type", this.type);
        details.put("quizAnswerId", this.quizAnswerId);

        return details.toString();
    }
}
