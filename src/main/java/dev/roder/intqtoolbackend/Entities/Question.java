package dev.roder.intqtoolbackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;

/**
 * Question is a class describing a question for a specific quiz.
 * It can have two types represented by Integers, 1 for multiple
 * choice and 2 for longtext answers.
 */

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer questionId;
    private Integer quizID;

    @Column(length = 2000)
    private String questionText;
    private Integer type;
    @OneToMany
    @JoinTable(name = "question_alternatives",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "alternativeid")
    )
    private Set<Alternative> alternatives;


    /**
     * Retrieves the quizId
     *
     * @return Returns the id of the quiz.
     */
    public Integer getQuizID() {
        return quizID;
    }

    /**
     * Sets the id of the quiz to a new value.
     *
     * @param quizID new value of the quizId
     */
    public void setQuizID(Integer quizID) {
        this.quizID = quizID;
    }


    /**
     * Retrieves the text of the question
     *
     * @return Returns the text of the question
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Sets the text of the question to a new value.
     *
     * @param questionText new value of the questionText
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Retrieves the type of the question( type 1 = multiple choice, type 2 = longText answer)
     *
     * @return Returns the type of the question.
     */
    public Integer getType() {
        return type;
    }

    /**
     * Sets the type to a new value.
     *
     * @param type new value of type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * Retrieves the questions set of alternatives.
     * If the question is of type 2 alternatives are irrelevant.
     *
     * @return Returns a set of alternatives.
     */
    public Set<Alternative> getAlternatives() {
        return alternatives;
    }

    /**
     * Sets the alternatives to a new value
     *
     * @param alternatives new value of the alternatives.
     */
    public void setAlternatives(Set<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

    /**
     * Retrieves the id of the question.
     *
     * @return Returns the id of the question.
     */
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     * Sets the id of the question to a new value
     *
     * @param questionId new value of the question Id
     */
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    /**
     * Builds JSONObject containing details of the question.
     * Contains questionId, questionText, type and alternatives
     * without rightAlternative field.
     *
     * @return Returns string version of the details JSONObject.
     */
    @Override
    public String toString() {
        JSONObject details = new JSONObject();
        details.put("questionId", this.questionId);
        details.put("questionText", this.questionText);
        details.put("type", this.type);
        JSONArray jsonAlternatives = new JSONArray();
        for (Alternative alternative : alternatives) {
            jsonAlternatives.put(alternative.toString());
        }
        details.put("alternatives", jsonAlternatives);


        return details.toString();
    }

    /**
     * Retrieves details of the question used when grading.
     * Details are used to build a JSON object.
     * Contains questionId, questionText, type, and alternatives with rightAlternative.
     *
     * @return String version of the JSON object containing details.
     */
    public String getEditDetails() {
        JSONObject details = new JSONObject();
        details.put("questionId", this.questionId);
        details.put("questionText", this.questionText);
        details.put("type", this.type);
        JSONArray jsonAlternatives = new JSONArray();
        for (Alternative alternative : alternatives) {
            jsonAlternatives.put(alternative.getDetailsForEdit());
        }
        details.put("alternatives", jsonAlternatives);


        return details.toString();
    }

    /**
     * Grades answers for questions of type 1 or type 3(multiple choice) automatically.
     *
     * @param answer answer to compare to the correct answer of the question.
     * @return Returns the grade of the answer(1 if the answer was correct, otherwise 0)
     */
    public Double autoGrade(String answer) {
        Double newGrade = 0.0;
        List<Alternative> correctAnswer = alternatives.stream().filter((Alternative::isRightAlternative)).collect(Collectors.toList());
        if (this.type == 1) {
            for (Alternative alternative : correctAnswer) {
                if (alternative.getAlternativeID() == Integer.parseInt(answer)) {
                    newGrade = 1.0;
                }
            }
        } else if (this.type == 3) {
            String[] alternativeIdsString = answer.replace(" ", "").split(",");
            List<Integer> alternativeIds = Arrays.stream(alternativeIdsString).map(id -> Integer.parseInt(id)).collect(Collectors.toList());
            for (Alternative alternative : correctAnswer) {
                if (alternativeIds.contains(alternative.getAlternativeID())) {
                    newGrade = newGrade + 1;
                } else {
                    newGrade = newGrade - 1;
                }
            }
            Long tempLong = Math.round(newGrade / correctAnswer.size() * 100);
            newGrade = tempLong.doubleValue() / 100;

            if (newGrade < 0) {
                newGrade = 0.0;
            }

        }


        return newGrade;
    }
}
