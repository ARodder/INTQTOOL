package dev.roder.INTQTOOLBackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.List;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer quizID;

    private String title;
    @OneToMany

    @JoinTable(name="quiz_questions",
            joinColumns = @JoinColumn(name="quiz_id"),
            inverseJoinColumns = @JoinColumn(name="question_id")
    )
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuizID () {
        return quizID;
    }

    public void setQuizID (Integer quizID) {
        this.quizID=quizID;
    }

    public List<Question> getQuestions () {
        return questions;
    }

    public void setQuestions (List<Question> questions) {
        this.questions=questions;
    }

    public String getDetails(){
        JSONObject details = new JSONObject();
        details.put("id",this.quizID);
        details.put("title",this.title);
        details.put("description",this.description);
        details.put("quizLength:",this.questions.size());


        return details.toString();
    }
    @Override
    public String toString(){
        JSONObject details = new JSONObject();
        details.put("id",this.quizID);
        details.put("title",this.title);
        details.put("description",this.description);
        details.put("quizLength",this.questions.size());
        details.put("author",this.author.getLastName()+", "+this.author.getFirstName());
        JSONArray jsonQuestions = new JSONArray();
        for(Question question:questions){
            jsonQuestions.put(question.toString());
        }
        details.put("questions",jsonQuestions);


        return details.toString();
    }
}
