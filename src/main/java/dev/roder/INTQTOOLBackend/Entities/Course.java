package dev.roder.INTQTOOLBackend.Entities;


import org.json.JSONObject;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer courseID;

    private String name;

    private String description;


    @ManyToMany(mappedBy = "courses")
    private Set<User> users = new LinkedHashSet<>();

    @Column(unique = true)
    private String joinCode;
    @OneToMany
    @JoinTable(name = "course_active_quizzes",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "deployment_id")
    )
    private List<DeployedQuiz> activeQuizzes;

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String courseName) {
        this.name = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public List<DeployedQuiz> getActiveQuizzes() {
        return activeQuizzes.stream().map((deployedQuiz) -> {
            if (deployedQuiz.getDeadline().compareTo(new Date())>0) {
                return deployedQuiz;
            } else {
                return null;
            }
        })
                .filter((deployedQuiz) -> deployedQuiz != null)
                .collect(Collectors.toList());
    }

    public void setActiveQuizzes(List<DeployedQuiz> activeQuizzes) {
        this.activeQuizzes = activeQuizzes;
    }
    public void addActiveQuiz(DeployedQuiz deployedQuiz) {
        this.activeQuizzes.add(deployedQuiz);
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    // TODO - you can skip creating your own to-JSON conversions. You will need to add some @JsonIgnore annotations for that
    public String getDetails() {
        JSONObject details = new JSONObject();
        details.put("id", this.courseID);
        details.put("name", this.name);
        details.put("description", description);
        details.put("joinCode", this.joinCode);
        details.put("activeQuizAmnt", this.activeQuizzes.size());

        return details.toString();
    }


}
