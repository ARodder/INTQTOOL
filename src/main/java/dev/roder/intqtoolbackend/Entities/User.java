package dev.roder.intqtoolbackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.*;

/**
 * Entity class representing a user from the database.
 * Contains id, encrypted password, settings, courses the user is in,
 * quizAnswers the user has created, username, email, roles, notifications,
 * firstName, lastName, expirationDate, accountLock and userEnabled.
 */
@Entity
public class User{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String password;
    private String settings;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_courses",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="course_id")
    )
    private List<Course> courses;
    @OneToMany
    private List<QuizAnswer> quizAnswers;
    @Column(unique=true)
    private String username;
    @Column(unique=true)
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles = new LinkedHashSet<>();
    @OneToMany
    private List<Notification> notifications;
    private String firstName;
    private String lastName;
    private LocalDate expirationDate;
    private boolean accountLock;
    private boolean userEnabled;


    /**
     * Empty constructor required for jpa.
     */
    public User(){

    }

    /**
     * Retrieves a list of all the users quizAnswers
     *
     * @return Returns a List of quizAnswers
     */
    public List<QuizAnswer> getQuizAnswers() {
        return quizAnswers;
    }
    public QuizAnswer getQuizAnswers(Integer quizID){

        try{
            return quizAnswers.stream()
                    .filter((quizAnswer)->(quizAnswer
                            .getDeployedQuiz()
                            .getId() == quizID
                            && quizAnswer
                            .getStatus()
                            .equals("in-progress")))
                    .collect(Collectors.toList()).get(0);
        } catch(Exception e){
            return null;
        }

    }

    /**
     * Adds a quizAnswer to the users list of quizAnswers
     *
     * @param qa quizAnswer to add to the users list.
     */
    public void addQuizAnswer(QuizAnswer qa){
        this.quizAnswers.add(qa);
    }

    /**
     * Sets the users quizAnswers to a new value
     *
     * @param quizAnswers new value of the quizAnswers list.
     */
    public void setQuizAnswers(List<QuizAnswer> quizAnswers) {
        this.quizAnswers = quizAnswers;
    }

    /**
     * Retrieves the id of the user.
     *
     * @return Returns the id
     */
    public Integer getId () {
        return id;
    }

    /**
     * Sets the id to a new value
     *
     * @param id new value of the id.
     */
    public void setId (Integer id) {
        this.id=id;
    }

    /**
     * Removes a role from the users role Set
     *
     * @param role role to remove from the set.
     */
    public void removeRole(Role role){
        this.roles.remove(role);
    }

    /**
     * Checks if the user has a certain role.
     *
     * @param roleName name of the role to check for
     * @return Returns boolean if the user contains a role or not.
     */
    public boolean hasRole(String roleName){
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList())
                .contains(roleName);
    }

    /**
     * Retrieves the users encrypted password.
     *
     * @return Returns encrypted password
     */
    public String getPassword () {
        return password;
    }

    /**
     * Sets the password to a new value
     *
     * @param password new value of the password
     */
    public void setPassword (String password) {
        this.password=password;
    }

    /**
     * Retrieves the users settings
     *
     * @return returns string representing the
     */
    public String getSettings () {
        return settings;
    }

    /**
     * Sets the settings to a new value
     *
     * @param settings new value of the settings
     */
    public void setSettings (String settings) {
        this.settings=settings;
    }

    /**
     * Retrieves the list of courses the user is in.
     *
     * @return list of courses
     */
    public List<Course> getCourses () {
        return courses;
    }

    /**
     * Sets the users list of courses to a new value.
     *
     * @param courses new value of the users course list
     */
    public void setCourses (List<Course> courses) {
        this.courses=courses;
    }

    /**
     * Retrieves the username of the user
     *
     * @return Returns the users username
     */
    public String getUsername() {
        return username;
    }


    /**
     * Sets the username to a new value.
     *
     * @param username new value of the username.
     */
    public void setUsername(String username) {
        this.username=username;
    }

    /**
     * Determine if the user is valid, has the required fields.
     *
     * @return Returns boolean depending on if the user is valid or not
     */
    public boolean isValid(){
        boolean valid = false;
        if((password != null && password.length() >= 8) &&
                (username != null && !username.isEmpty()) &&
                (email != null && !email.isEmpty()) &&
                (firstName != null && !firstName.isEmpty()) &&
                (lastName != null && !lastName.isEmpty())){
            valid = true;
        }
        return valid;
    }

    /**
     * Sets the roles to a new value.
     *
     * @param roles new value of the roles
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Retrieves the user's roles
     *
     * @return Returns the users role(s)
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Adds a role to the user's role set
     *
     * @param role role to add to the user.
     */
    public void addRole(Role role) {
        roles.add(role);
    }

    /**
     * Retrieves the expirationDate of the user
     *
     * @return Returns the expirationDate
     */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expirationDate to a new value.
     *
     * @param expirationDate new value for the expirationDate
     */
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Checks if the account is logged
     *
     * @return Returns boolean depending on if the account is locked
     */
    public boolean isAccountLock() {
        return !accountLock;
    }

    /**
     * Sets the accountLock to a new value
     *
     * @param accountLock new value of the accountLock
     */
    public void setAccountLock(boolean accountLock) {
        this.accountLock = accountLock;
    }

    /**
     * Retrieves the email of the user.
     *
     * @return Returns the email-address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email to a new value
     *
     * @param email new value of the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Checks if the user is enabled.
     *
     * @return Returns boolean depending on if the user is enabled or not
     */
    public boolean isUserEnabled() {
        return userEnabled;
    }

    /**
     * Sets the userEnabled to a new value.
     *
     * @param userEnabled new value of the userEnabled
     */
    public void setUserEnabled(boolean userEnabled) {
        this.userEnabled = userEnabled;
    }

    /**
     * Retrieves the first name of the user
     *
     * @return Returns the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the firstNam of the user to a new value.
     *
     * @param firstName new value of the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the last name of the user.
     *
     * @return returns the last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user to a new value
     *
     * @param lastname new value of the lastname
     */
    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    /**
     * Retrieves a user's list of notifications
     *
     * @return Returns list of notifications
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public List<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Sets the notifications to a new list value
     *
     * @param notifications new list value for the notifications
     */
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     * Adds a notification to the users notification list if it
     * is not already in the list.
     *
     * @param notification notification to potentially add to the list.
     */
    public void addNotification(Notification notification){
        if(!notifications.contains(notification)){
            this.notifications.add(notification);
        }

    }

    /**
     * Removes a specific notification from the user's list of notifications
     *
     * @param notification notification to remove.
     */
    public void removeNotification(Notification notification){
        this.notifications.remove(notification);
    }

    /**
     * Removes a specific notification from the user's list of notifications using notificationID
     *
     * @param notificationID id of the notification to remove
     */
    public void removeNotification(Integer notificationID){
        this.notifications = this.notifications.stream()
                .filter(notification -> notification.getNotificationID() != notificationID)
                .collect(Collectors.toList());
    }

    /**
     * Adds a course to the user's list of courses it participates in
     *
     * @param course course to add.
     */
    public void addCourse(Course course){
        this.courses.add(course);
    }

    /**
     * Builds a JSONObject of the user.
     * Contains id, username, firstname,
     * lastname, email, courses, roles and notifications
     *
     * @return Returns a string version of the JSONObject
     */
    @Override
    public String toString() {

        try{
            JSONObject userObject = new JSONObject();
            userObject.put("id",id);
            userObject.put("username",username);
            userObject.put("firstName",firstName);
            userObject.put("lastName",lastName);
            JSONArray courseJsonArray = new JSONArray();
            for(Course course:courses){
                courseJsonArray.put(course.getCourseID());
            }
            userObject.put("courses",courseJsonArray);
            userObject.put("email",email);

            JSONArray roleJsonArray = new JSONArray();
            for(Role role:roles){
                roleJsonArray.put(role.getName());
            }
            userObject.put("roles",roleJsonArray);
            JSONArray notificationJsonArray = new JSONArray();
            for(Notification notification:notifications){
                notificationJsonArray.put(notification.getDetails());
            }
            userObject.put("notifications",notificationJsonArray);

            return userObject.toString();
        }catch(Exception e){
            return "Unavailable";
        }


    }

    /**
     * Builds a JSONObject of the user.
     * Contains id, username, firstname,
     * lastname, email and roles.
     *
     * @return Returns a string version of the JSONObject
     */
    public String getDetails(){
        JSONObject userObject = new JSONObject();
        userObject.put("id",id);
        userObject.put("username",username);
        userObject.put("firstName",firstName);
        userObject.put("lastName",lastName);
        userObject.put("email",email);
        JSONArray roleJsonArray = new JSONArray();
        for(Role role:roles){
            roleJsonArray.put(role.getName());
        }
        userObject.put("roles",roleJsonArray);


        return userObject.toString();
    }


}
