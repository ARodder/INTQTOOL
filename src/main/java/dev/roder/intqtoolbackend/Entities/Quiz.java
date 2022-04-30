package dev.roder.intqtoolbackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column(length=2000)
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

    public List<Integer> getQuestionIds(){
        return questions.stream().map((question)->question.getQuestionId()).collect(Collectors.toList());
    }

    public String getDetails(){
        JSONObject details = new JSONObject();
        details.put("quizID",this.quizID);
        details.put("title",this.title);
        details.put("description",this.description);
        details.put("quizLength",this.questions.size());
        details.put("author",this.author.getLastName()+", "+this.author.getFirstName());


        return details.toString();
    }
    @Override
    public String toString(){
        JSONObject details = new JSONObject();
        details.put("quizID",this.quizID);
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

    public String getEditDetails(){
        JSONObject details = new JSONObject();
        details.put("quizID",this.quizID);
        details.put("title",this.title);
        details.put("description",this.description);
        details.put("quizLength",this.questions.size());
        details.put("author",this.author.getLastName()+", "+this.author.getFirstName());
        JSONArray jsonQuestions = new JSONArray();
        for(Question question:questions){
            jsonQuestions.put(question.getEditDetails());
        }
        details.put("questions",jsonQuestions);


        return details.toString();
    }
}
