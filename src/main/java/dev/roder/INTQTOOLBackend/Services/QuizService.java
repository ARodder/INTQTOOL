package dev.roder.INTQTOOLBackend.Services;

import dev.roder.INTQTOOLBackend.Entities.Course;
import dev.roder.INTQTOOLBackend.Entities.DeployedQuiz;
import dev.roder.INTQTOOLBackend.Entities.Quiz;
import dev.roder.INTQTOOLBackend.Entities.User;
import dev.roder.INTQTOOLBackend.Repositories.DeployedQuizRepository;
import dev.roder.INTQTOOLBackend.Repositories.QuizRepository;
import dev.roder.INTQTOOLBackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeployedQuizRepository deployedQuizRepository;


    public String getQuiz(Integer deplyedQuizID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        DeployedQuiz foundQuiz = deployedQuizRepository.findById(deplyedQuizID).get();

        List<Course> userCourses= currentUser.getCourses();

        boolean userInCourse = false;
        for(Course course:userCourses){
            if(course.getActiveQuizzes().contains(foundQuiz)){
                userInCourse = true;
            }
        }

        if(userInCourse){
            return foundQuiz.toString();
        }else{
            return "";
        }

    }

    public void addQuiz(Quiz quiz){
        quizRepository.save(quiz);
    }
}
