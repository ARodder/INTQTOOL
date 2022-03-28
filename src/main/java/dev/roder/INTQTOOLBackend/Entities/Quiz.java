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

    @ManyToOne
    private User author;

    private String description;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
