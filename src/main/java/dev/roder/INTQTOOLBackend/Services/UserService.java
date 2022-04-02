package dev.roder.INTQTOOLBackend.Services;

import dev.roder.INTQTOOLBackend.Entities.Course;
import dev.roder.INTQTOOLBackend.Entities.Quiz;
import dev.roder.INTQTOOLBackend.Entities.Role;
import dev.roder.INTQTOOLBackend.Entities.User;
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

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(User user){
        user.setSettings("0,0,0,0,0");
        user.addRole(new Role("ROLE_STUDENT"));
        user.setExpirationDate(LocalDate.now().plusYears(5));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccountLock(false);
        user.setUserEnabled(true);
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
}
