package dev.roder.intqtoolbackend.Entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * Entity class representing an alternative object stored in the database.
 * Used in Question object to represent answer alternative for multiple choice questions(type 1).
 */
@Entity
public class Alternative {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer alternativeID;
    private String alternative;
    private boolean rightAlternative;

    /**
     * No args constructor required by jpa
     */
    public Alternative(){

    }


    public String getAlternative() {
        return alternative;
    }

    /**
     * Set the text of the alternative
     *
     * @param alternative new text of the alternative.
     */
    public void setAlternative(String alternative) {
        this.alternative = alternative;
    }

    /**
     * Retrieves the id of the alternative.
     *
     * @return id of the alternative
     */
    public Integer getAlternativeID() {
        return alternativeID;
    }

    /**
     * Sets the id of the alternative to a new value
     *
     * @param alternativeID new value of the alternative id
     */
    public void setAlternativeID(Integer alternativeID) {
        this.alternativeID = alternativeID;
    }

    /**
     * Checks if this instance of the alternative is the correct answer.
     *
     * @return Returns true if this answer is marked as correct, otherwise false.
     */
    public boolean isRightAlternative() {
        return rightAlternative;
    }


    /**
     * Set the alternative to either true or false based on if it should be correct for its given question.
     *
     * @param rightAlternative new value of the right alternative.
     */
    public void setRightAlternative(boolean rightAlternative) {
        this.rightAlternative = rightAlternative;
    }

    /**
     * Builds a JSON object with only the id and the alternative text
     * without the rightAlternative field.
     *
     * @return Returns a string version of the JSON object.
     */
    @Override
    public String toString(){
        JSONObject details = new JSONObject();
        details.put("id",alternativeID);
        details.put("alternative",alternative);

        return details.toString();

    }

    /**
     * Builds a JSON object with the id, rightAlternative and the alternative text.
     *
     * @return Returns a string version of the JSON object.
     */
    public String getDetailsForEdit(){
        JSONObject details = new JSONObject();
        details.put("id",alternativeID);
        details.put("alternative",alternative);
        details.put("rightAlternative",rightAlternative);

        return details.toString();

    }

}
