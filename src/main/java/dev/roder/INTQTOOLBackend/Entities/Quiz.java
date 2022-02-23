package dev.roder.INTQTOOLBackend.Entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String quizID;
    @OneToMany
    private List<Question> questions;

    public String getQuizID () {
        return quizID;
    }

    public void setQuizID (String quizID) {
        this.quizID=quizID;
    }

    public List<Question> getQuestions () {
        return questions;
    }

    public void setQuestions (List<Question> questions) {
        this.questions=questions;
    }
}
