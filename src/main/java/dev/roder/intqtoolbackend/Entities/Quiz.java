package dev.roder.intqtoolbackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity class representing a quiz stored in the database.
 * Contains quizId, title, a list of questions, an author and description
 *
 */
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


    /**
     * Retrieves the author of the quiz
     *
     * @return Returns the user that authored the quiz.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Sets the author to a new value
     *
     * @param author new value of the author
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Retrieves the description of the quiz.
     *
     * @return Returns the description of the quiz.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the title of the quiz
     *
     * @return Returns the title of the quiz.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title to a new value
     *
     * @param title new value of the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the description to a new value
     *
     * @param description new value of the description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the value of the quizID
     *
     * @return the id of the quiz
     */
    public Integer getQuizID () {
        return quizID;
    }

    /**
     * Sets the quizId to a new value.
     *
     * @param quizID new value of the quizId
     */
    public void setQuizID (Integer quizID) {
        this.quizID=quizID;
    }

    /**
     *Retrieves a list of all the questions in the quiz.
     *
     * @return Returns a list of questions.
     */
    public List<Question> getQuestions () {
        return questions;
    }

    /**
     * Sets the questions to a new value.
     *
     * @param questions new value of the questions.
     */
    public void setQuestions (List<Question> questions) {
        this.questions=questions;
    }

    /**
     * Retrieves a list of the ids of all the questions in the quiz.
     *
     * @return returns a list of integers containing the id of each question
     */
    public List<Integer> getQuestionIds(){
        return questions.stream().map(Question::getQuestionId).collect(Collectors.toList());
    }

    /**
     * Builds JSONObject containing details from the quiz.
     * contains id, title, description, quizLength and authors name.
     *
     * @return Returns string version of the JSONObject
     */
    public String getDetails(){
        JSONObject details = new JSONObject();
        details.put("quizID",this.quizID);
        details.put("title",this.title);
        details.put("description",this.description);
        details.put("quizLength",this.questions.size());
        details.put("author",this.author.getLastName()+", "+this.author.getFirstName());


        return details.toString();
    }

    /**
     * Builds JSONObject containing details from the quiz.
     * contains id, title, description, quizLength, questions and authors name.
     *
     * @return Returns string version of the JSONObject
     */
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

    /**
     * Builds JSONObject containing details from the quiz.
     * contains id, title, description, quizLength, questions with fields for grading and authors name.
     *
     * @return Returns string version of the JSONObject
     */
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
