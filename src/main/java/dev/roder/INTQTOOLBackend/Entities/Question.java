package dev.roder.INTQTOOLBackend.Entities;

import java.util.List;
import javax.persistence.*;

@Entity
public class Question {

    @Id
    private String quizID;
    private String questionText;
    private String type;
    private String alternatives;

    private String questionID;

    public String getQuizID () {
        return quizID;
    }

    public void setQuizID (String quizID) {
        this.quizID=quizID;
    }

    public String getQuestionText () {
        return questionText;
    }

    public void setQuestionText (String questionText) {
        this.questionText=questionText;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type=type;
    }

    public String getAlternatives () {
        return alternatives;
    }

    public void setAlternatives (String alternatives) {
        this.alternatives=alternatives;
    }

    public String getQuestionID () {
        return questionID;
    }

    public void setQuestionID (String questionID) {
        this.questionID=questionID;
    }
}
