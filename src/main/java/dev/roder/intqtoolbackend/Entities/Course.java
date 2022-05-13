package dev.roder.intqtoolbackend.Entities;


import org.json.JSONObject;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Entity class representing a course stored in a database.
 * Used to hold a list of deployed quizzes and users,
 * and name of the course, description and joinCode.
 */
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

    /**
     * Retrieves the id of the course
     *
     * @return Returns id of the course.
     */

    public Integer getCourseID() {
        return courseID;
    }

    /**
     * Sets the id of the course to a new value.
     *
     * @param courseID new value to set the id f the course to.
     */
    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }

    /**
     * Retrieves the name of the course.
     *
     * @return Returns the name of the course
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the course to a new value.
     *
     * @param courseName new value to set the name of the course to.
     */

    public void setName(String courseName) {
        this.name = courseName;
    }

    /**
     * Retrieves the description of the course.
     *
     * @return Returns the description of the course.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the course to a new value
     *
     * @param description new value to set the description of the course to.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the joinCode of the course
     *
     * @return Returns the joinCode of the course
     */
    public String getJoinCode() {
        return joinCode;
    }

    /**
     * Sets the joinCode of the course to a new value.
     *
     * @param joinCode new value to set the joinCode to.
     */
    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    /**
     * Retrieves the active quizzes of the course by filtering out quizzes with a passed deadline.
     *
     * @return Returns a list of the active quizzes of the course.
     */
    public List<DeployedQuiz> getActiveQuizzes() {
        return activeQuizzes.stream().map((deployedQuiz) -> {
            if (deployedQuiz.getDeadline().compareTo(new Date())>0 && deployedQuiz.getQuiz().getQuestions().size()>0) {
                return deployedQuiz;
            } else {
                return null;
            }
        })
                .filter((deployedQuiz) -> deployedQuiz != null)
                .collect(Collectors.toList());
    }

    public List<DeployedQuiz> getArchivedQuizzes() {
        return activeQuizzes.stream().map((deployedQuiz) -> {
            if (deployedQuiz.getDeadline().compareTo(new Date())<0 && deployedQuiz.getQuiz().getQuestions().size()>0) {
                return deployedQuiz;
            } else {
                return null;
            }
        })
                .filter((deployedQuiz) -> deployedQuiz != null)
                .collect(Collectors.toList());
    }

    /**
     * Sets the active quizzes to a new value
     *
     * @param activeQuizzes new value of the activeQuizzes
     */

    public void setActiveQuizzes(List<DeployedQuiz> activeQuizzes) {
        this.activeQuizzes = activeQuizzes;
    }

    /**
     * Add a deployedQuiz to the courses activeQuizzes list.
     *
     * @param deployedQuiz the new deployedQuiz to add to the courses activeQuizzes list.
     */
    public void addActiveQuiz(DeployedQuiz deployedQuiz) {
        this.activeQuizzes.add(deployedQuiz);
    }

    /**
     * Retrieves a set of users that is in the specified course
     *
     * @return Returns the set of users that are in the course.
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Sets the users-Set to a new value.
     *
     * @param users new value to set the users-Set to.
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /**
     * Adds a new user to the course.
     *
     * @param user New user to add to the course
     */
    public void addUser(User user) {
        this.users.add(user);
    }

    /**
     * Builds JSON object containing the details of the course.
     *
     * @return Returns a string version of the JSON object containing the course details.
     */
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
