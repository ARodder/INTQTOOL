package dev.roder.INTQTOOLBackend.Services;

import dev.roder.INTQTOOLBackend.Entities.*;
import dev.roder.INTQTOOLBackend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public String getQuizDetails(Integer deplyedQuizID){
        // TODO - code duplication here, extract a method
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
            return foundQuiz.getDetailsForEdit();
        }else{
            return "";
        }

    }

    // TODO - perhaps too much happening here?
    // TODO - what does this method actually do? Does the name represent that well?
    // TODO - in general, you should split the logic into smaller pieces. For example, if you update "deployment of the quiz in a course", update only that.
    //  If you update the title of a quiz, update only the quiz details, nothing changes in the deployment (deadlines, etc)
    public Integer addQuiz(DeployedQuiz quiz,Integer courseId){
        User currentUser = getCurrentUser();
        Course quizCourse = courseRepository.findById(courseId).get();
        if(quiz.getId() != null){
            DeployedQuiz existingQuiz = deployedQuizRepository.findById(quiz.getId()).get();
            if(quiz.getDeployedQuiz().getQuizID() !=null){
                // TODO isn't existingDeployedQuiz redundant?
                // I.e., existingDeployedQuiz == quiz.getDeployedQuiz(), isn't it?
                Quiz existingDeployedQuiz = quizRepository.findById(quiz.getDeployedQuiz().getQuizID()).get();
                existingDeployedQuiz.setTitle(quiz.getDeployedQuiz().getTitle());
                existingDeployedQuiz.setDescription(quiz.getDeployedQuiz().getDescription());
                quizRepository.save(existingDeployedQuiz);
            }

            existingQuiz.setDeploymentCourse(quizCourse);
            existingQuiz.setDeadline(quiz.getDeadline());
            deployedQuizRepository.save(existingQuiz);

        }else{
            quiz.setDeploymentCourse(quizCourse);
            quiz.getDeployedQuiz().setAuthor(currentUser);
            quiz.getDeployedQuiz().setQuestions(new ArrayList<>());
            quiz.setQuizAnswer(new ArrayList<>());
            quizRepository.save(quiz.getDeployedQuiz());
            deployedQuizRepository.save(quiz);
            quizCourse.addActiveQuiz(quiz);
            courseRepository.save(quizCourse);
        }

        return quiz.getId();
    }

    // TODO - why is this method inside QuizService? This is an authentication-stuff. Perhaps in UserService or AuthService
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        return userRepository.findByUsername(currentPrincipalName).get();
    }

    // TODO - this smells like "too much happening here". alternativRepository.save, questionRepository.save, quizRepository.save, deployedQuizRepository.save
    public void saveQuiz(DeployedQuiz quiz,Integer courseId){
        User currentUser = getCurrentUser();
        DeployedQuiz updatedDeployment = deployedQuizRepository.findById(quiz.getId()).get();
        Quiz newInfo = quiz.getDeployedQuiz();
        newInfo.setAuthor(currentUser);
        Quiz quizToUpDate = quizRepository.findById(newInfo.getQuizID()).get();
        if(currentUser.getCourses().contains(updatedDeployment.getDeploymentCourse())){
            if(updatedDeployment.getDeploymentCourse().getCourseID() != courseId){
                updatedDeployment.setDeploymentCourse(courseRepository.findById(courseId).get());
            }
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
            deployedQuizRepository.save(updatedDeployment);
        }

    }

    public String getQuestionAnswers(Integer deployedQuizId){
        Optional<DeployedQuiz> foundDeployedQuiz = deployedQuizRepository.findById(deployedQuizId);
        if(foundDeployedQuiz.isPresent()){
            DeployedQuiz deployedQuiz = foundDeployedQuiz.get();
            return deployedQuiz.getQuestionAnswers();
        }else{
            throw new NoSuchElementException("That quiz is not found in deployment");
        }

    }
}
