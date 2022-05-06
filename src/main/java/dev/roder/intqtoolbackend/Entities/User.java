package dev.roder.intqtoolbackend.Entities;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.*;

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


    public User(){

    }

    public List<QuizAnswer> getQuizAnswers() {
        return quizAnswers;
    }
    public QuizAnswer getQuizAnswers(Integer quizID){

        try{
            return quizAnswers.stream().filter((quizAnswer)->(quizAnswer.getDeployedQuiz().getQuiz().getQuizID() == quizID && quizAnswer.getStatus().equals("in-progress"))).collect(Collectors.toList()).get(0);
        } catch(Exception e){
            return null;
        }

    }

    public void addQuizAnswer(QuizAnswer qa){
        this.quizAnswers.add(qa);
    }

    public void setQuizAnswers(List<QuizAnswer> quizAnswers) {
        this.quizAnswers = quizAnswers;
    }

    public Integer getId () {
        return id;
    }
    public void setId (Integer id) {
        this.id=id;
    }

    public void removeRole(Role role){
        this.roles.remove(role);
    }

    public boolean hasRole(String roleName){
        return roles.stream().map((role)->role.getName()).collect(Collectors.toList()).contains(roleName);
    }
    public String getPassword () {
        return password;
    }
    public void setPassword (String passHash) {
        this.password=passHash;
    }
    public String getSettings () {
        return settings;
    }
    public void setSettings (String settings) {
        this.settings=settings;
    }
    public List<Course> getCourses () {
        return courses;
    }
    public void setCourses (List<Course> courses) {
        this.courses=courses;
    }
    public String getUsername() {
        return username;
    }


    public void setUsername(String userName) {
        this.username=userName;
    }
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
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void addRole(Role role) {
        roles.add(role);
    }
    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
    public boolean isAccountLock() {
        return !accountLock;
    }
    public void setAccountLock(boolean accountLock) {
        this.accountLock = accountLock;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isUserEnabled() {
        return userEnabled;
    }
    public void setUserEnabled(boolean userEnabled) {
        this.userEnabled = userEnabled;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void addNotification(Notification notification){
        if(!notifications.contains(notification)){
            this.notifications.add(notification);
        }

    }
    public void removeNotification(Notification notification){
        this.notifications.remove(notification);
    }

    public void removeNotification(Integer notificationID){
        this.notifications = this.notifications.stream().filter(notification -> notification.getNotificationID() != notificationID).collect(Collectors.toList());
    }

    public void addCourse(Course course){
        this.courses.add(course);
    }

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
