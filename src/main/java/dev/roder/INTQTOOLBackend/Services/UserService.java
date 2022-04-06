package dev.roder.INTQTOOLBackend.Services;

import dev.roder.INTQTOOLBackend.Entities.*;
import dev.roder.INTQTOOLBackend.Repositories.CourseRepository;
import dev.roder.INTQTOOLBackend.Repositories.RoleRepository;
import dev.roder.INTQTOOLBackend.Repositories.UserRepository;
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
            currentUser.setNotifications(new ArrayList<>());

            userRepository.save(currentUser);
            clearSuccess = true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            clearSuccess = false;
        }

        return clearSuccess;
    }

    public boolean removeNotification(String notificationID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        boolean delSuccess = false;

        try{
            List<String> notificationIDs = currentUser.getNotifications().stream().map(notification -> notification.getNotificationID()).collect(Collectors.toList());

            if(notificationIDs.contains(notificationID)){
                currentUser.removeNotification(notificationID);
                delSuccess = true;
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
            delSuccess = false;
        }

        return delSuccess;
    }
}
