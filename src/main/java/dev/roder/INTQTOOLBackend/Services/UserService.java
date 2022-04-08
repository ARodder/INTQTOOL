package dev.roder.INTQTOOLBackend.Services;

import dev.roder.INTQTOOLBackend.Entities.*;
import dev.roder.INTQTOOLBackend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private QuizAnswerRepository quizAnswerRepository;

    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(User user){
        Role role = new Role("ROLE_STUDENT");

        user.setSettings("0,0,0,0,0");
        user.addRole(role);
        user.setExpirationDate(LocalDate.now().plusYears(5));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccountLock(false);
        user.setUserEnabled(true);
        roleRepository.save(role);
        userRepository.save(user);
    }

    public ArrayList<String> getAllUsers(){
        ArrayList<String> allUsers = new ArrayList<String>();

        Iterator<User> it = userRepository.findAll().iterator();

        while(it.hasNext()){
            allUsers.add(it.next().toString());
        }

        return allUsers;
    }

    public String getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        return currentUser.toString();

    }

    public List<String> getUsersActiveQuizes(){
        List<String> quizzes = new ArrayList<String>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        for(Course course: currentUser.getCourses()){
            for(Quiz quiz :course.getActiveQuizzes()){
                quizzes.add(quiz.getDetails());
            }
        }

        return quizzes;
    }

    public List<String> getUsersCourses(){
        List<String> courses = new ArrayList<String>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        for(Course course: currentUser.getCourses()){
            courses.add(course.getDetails());
        }

        return courses;
    }

    public List<String> getNotifications(){
        List<String> notifications = new ArrayList<String>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        for(Notification notification:currentUser.getNotifications()){
            notifications.add(notification.getDetails());
        }

        return notifications;
    }

    public boolean joinCourse(String joinCode){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();
        boolean joinSuccess = false;


        try{
            Course newCourse = courseRepository.findByJoinCode(joinCode).get();
            System.out.println(newCourse.getCourseName());
            if(newCourse != null){
                currentUser.addCourse(newCourse);
                userRepository.save(currentUser);
            }
            joinSuccess = true;
        } catch(Exception e){
            System.out.println(e.getMessage());
            joinSuccess = false;
        }

        return joinSuccess;
    }

    public boolean clearNotifications(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        boolean clearSuccess = false;

        try{
            List<Notification> currentUserNotifications = currentUser.getNotifications().stream().map((notification)-> notification).collect(Collectors.toList());


            currentUser.setNotifications(new ArrayList<>());

            userRepository.save(currentUser);

            for(Notification notification: currentUserNotifications){
                notificationRepository.delete(notification);
            }
            clearSuccess = true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            clearSuccess = false;
        }

        return clearSuccess;
    }

    public boolean removeNotification(Integer notificationID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        boolean delSuccess = false;

        try{
            List<Integer> notificationIDs = currentUser.getNotifications().stream().map(notification -> notification.getNotificationID()).collect(Collectors.toList());

            if(notificationIDs.contains(notificationID)){
                currentUser.removeNotification(notificationID);
                notificationRepository.delete(notificationRepository.findByNotificationID(notificationID).get());
                delSuccess = true;
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
            delSuccess = false;
        }

        return delSuccess;
    }

    public String getUserQuizAnswers(Integer quizID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        QuizAnswer qa = currentUser.getQuizAnswers(quizID);

        if(qa != null){
            return qa.toString();
        } else {
            return "No answer";
        }


    }

    public boolean saveUserQuizAnswer(QuizAnswer qa){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        boolean saveSuccess = false;

        try{
            qa.getAnswers().forEach((ans)->questionAnswerRepository.save(ans));
            quizAnswerRepository.save(qa);
            currentUser.addQuizAnswer(qa);
            userRepository.save(currentUser);
            saveSuccess = true;
        } catch (Exception e){
            System.out.println(e);
            saveSuccess = false;
        }

        return saveSuccess;
    }
}
