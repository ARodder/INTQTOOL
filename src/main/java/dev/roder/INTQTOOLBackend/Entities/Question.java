package dev.roder.INTQTOOLBackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Question {

    @Id
    private String questionID;
    private String quizID;
    private String questionText;
    private String type;
    @OneToMany
    private Set<Alternativ> alternatives;


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

    public Set<Alternativ> getAlternatives () {
        return alternatives;
    }

    public void setAlternatives (Set<Alternativ> alternatives) {
        this.alternatives=alternatives;
    }

    public String getQuestionID () {
        return questionID;
    }

    public void setQuestionID (String questionID) {
        this.questionID=questionID;
    }

    @Override
    public String toString(){
        JSONObject details = new JSONObject();
        details.put("id",this.questionID);
        details.put("question",this.questionText);
        details.put("type",this.type);
        JSONArray jsonAlternatives = new JSONArray();
        for(Alternativ alternativ:alternatives){
            jsonAlternatives.put(alternativ.toString());
        }
        details.put("questions",jsonAlternatives);


        return details.toString();
    }
}
