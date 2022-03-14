package dev.roder.INTQTOOLBackend.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class QuestionAnswer {

    @Id
    private int id;
    private String userID;
    private String courseID;
    private String answer;
    private String status;
    private String feedback;
}
