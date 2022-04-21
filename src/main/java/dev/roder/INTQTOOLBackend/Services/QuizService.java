package dev.roder.INTQTOOLBackend.Services;

import dev.roder.INTQTOOLBackend.Entities.*;
import dev.roder.INTQTOOLBackend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeployedQuizRepository deployedQuizRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AlternativRepository alternativRepository;
    @Autowired
    private QuestionRepository questionRepository;


    public String getQuiz(Integer deplyedQuizID){
        User currentUser = getCurrentUser();

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

    public Integer addQuiz(DeployedQuiz quiz,Integer courseId){
        User currentUser = getCurrentUser();
        Course quizCourse = courseRepository.findById(courseId).get();
        quiz.setDeploymentCourse(quizCourse);
        quiz.getDeployedQuiz().setAuthor(currentUser);
        quiz.getDeployedQuiz().setQuestions(new ArrayList<>());
        quiz.setQuizAnswer(new ArrayList<>());
        quizRepository.save(quiz.getDeployedQuiz());
        deployedQuizRepository.save(quiz);
        quizCourse.addActiveQuiz(quiz);
        courseRepository.save(quizCourse);
        return quiz.getId();
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        return userRepository.findByUsername(currentPrincipalName).get();
    }

    public void saveQuiz(DeployedQuiz quiz){
        User currentUser = getCurrentUser();
        DeployedQuiz updatedDeployment = deployedQuizRepository.findById(quiz.getId()).get();
        Quiz newInfo = quiz.getDeployedQuiz();
        newInfo.setAuthor(currentUser);
        Quiz quizToUpDate = quizRepository.findById(newInfo.getQuizID()).get();
        if(currentUser.getCourses().contains(updatedDeployment.getDeploymentCourse())){
            for(Question question: newInfo.getQuestions()){
                question.setQuizID(quizToUpDate.getQuizID());
                if(question.getType() == 1){
                    for(Alternativ alternative: question.getAlternatives()){
                        alternativRepository.save(alternative);
                    }
                }else{
                    question.setAlternatives(new HashSet<>());
                }
                questionRepository.save(question);
            }
            quizToUpDate.setQuestions(newInfo.getQuestions());
            quizRepository.save(quizToUpDate);
        }

    }
}
