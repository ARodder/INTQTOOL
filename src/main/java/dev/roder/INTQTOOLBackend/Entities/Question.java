package dev.roder.INTQTOOLBackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;
import javax.persistence.*;

/**
 * Question is a class describing a question for a specific quiz.
 * It can have two types represented by Integers, 1 for multiple
 * choice and 2 for longtext answers.
 */

@Entity
public class Question {

    @Id
    private Integer questionID;
    private Integer quizID;

    @Column(length=2000)
    private String questionText;
    private Integer type;
    @OneToMany
    @JoinTable(name="question_alternatives",
            joinColumns = @JoinColumn(name="question_id"),
            inverseJoinColumns = @JoinColumn(name="alternative_id")
    )
    private Set<Alternativ> alternatives;


    public Integer getQuizID () {
        return quizID;
    }

    public void setQuizID (Integer quizID) {
        this.quizID=quizID;
    }

    public String getQuestionText () {
        return questionText;
    }

    public void setQuestionText (String questionText) {
        this.questionText=questionText;
    }

    public Integer getType () {
        return type;
    }

    public void setType (Integer type) {
        this.type=type;
    }

    public Set<Alternativ> getAlternatives () {
        return alternatives;
    }

    public void setAlternatives (Set<Alternativ> alternatives) {
        this.alternatives=alternatives;
    }

    public Integer getQuestionID () {
        return questionID;
    }

    public void setQuestionID (Integer questionID) {
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
        details.put("alternatives",jsonAlternatives);


        return details.toString();
    }
}
