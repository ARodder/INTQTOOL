package dev.roder.intqtoolbackend.Services;

import dev.roder.intqtoolbackend.Entities.*;
import dev.roder.intqtoolbackend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service class containing most of the functionality
 * for any endpoint with the "/quiz/" prefix
 */
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
    private AlternativeRepository alternativeRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private QuizAnswerRepository quizAnswerRepository;


    /**
     * Retrieves a specific deployment of a quiz using deploymentId
     *
     * @param deplyedQuizID id of the deployedQuiz to retrieve.
     * @return Returns the deployedQuiz
     */
    public String getQuiz(Integer deplyedQuizID) {
        User currentUser = getCurrentUser();

        DeployedQuiz foundQuiz = deployedQuizRepository.findById(deplyedQuizID).orElse(null);

        List<Course> userCourses = currentUser.getCourses();

        boolean userInCourse = false;
        for (Course course : userCourses) {
            if (course.getActiveQuizzes().contains(foundQuiz)) {
                userInCourse = true;
            }
        }

        if (foundQuiz != null && userInCourse) {
            return foundQuiz.toString();
        } else {
            return "";
        }

    }

    /**
     * Retrieves details for a specific deployedQuiz
     * used when editing a quiz.
     *
     * @param deployedQuizID id of deployedQuiz
     * @return Returns the details needed for editing a quiz
     */
    public String getQuizDetails(Integer deployedQuizID) {
        User currentUser = getCurrentUser();

        DeployedQuiz foundQuiz = deployedQuizRepository.findById(deployedQuizID).get();

        List<Course> userCourses = currentUser.getCourses();

        boolean userInCourse = false;
        for (Course course : userCourses) {
            if (course.getActiveQuizzes().contains(foundQuiz)) {
                userInCourse = true;
            }
        }

        if (userInCourse) {
            return foundQuiz.getDetailsForEdit();
        } else {
            return "";
        }

    }

    /**
     * Creates a new quiz and deploys it to a course.
     *
     * @param quiz     new quiz to add
     * @param courseId id of the course to add it to
     * @return returns the id of the newly created deployedQuiz
     */
    public Integer addQuiz(DeployedQuiz quiz, Integer courseId) {
        User currentUser = getCurrentUser();
        Course quizCourse = courseRepository.findById(courseId).get();
        if (quiz.getId() != null) {
            DeployedQuiz existingQuiz = deployedQuizRepository.findById(quiz.getId()).get();
            if (quiz.getQuiz().getQuizID() != null) {
                Quiz existingDeployedQuiz = quizRepository.findById(quiz.getQuiz().getQuizID()).get();
                existingDeployedQuiz.setTitle(quiz.getQuiz().getTitle());
                existingDeployedQuiz.setDescription(quiz.getQuiz().getDescription());
                quizRepository.save(existingDeployedQuiz);
            }

            existingQuiz.setDeploymentCourse(quizCourse);
            existingQuiz.setDeadline(quiz.getDeadline());
            deployedQuizRepository.save(existingQuiz);

        } else {
            quiz.setDeploymentCourse(quizCourse);
            quiz.getQuiz().setAuthor(currentUser);
            quiz.getQuiz().setQuestions(new ArrayList<>());
            quiz.setQuizAnswer(new ArrayList<>());
            quizRepository.save(quiz.getQuiz());
            deployedQuizRepository.save(quiz);
            quizCourse.addActiveQuiz(quiz);
            courseRepository.save(quizCourse);
        }

        return quiz.getId();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        return userRepository.findByUsername(currentPrincipalName).get();
    }

    /**
     * Saves or updates a quiz, or which course it is deployed to.
     *
     * @param quiz     quiz to save
     * @param courseId id of the course to deploy the quiz to
     */
    public void saveQuiz(DeployedQuiz quiz, Integer courseId) {
        User currentUser = getCurrentUser();
        DeployedQuiz updatedDeployment = deployedQuizRepository.findById(quiz.getId()).get();
        Quiz newInfo = quiz.getQuiz();
        newInfo.setAuthor(currentUser);
        Quiz quizToUpDate = quizRepository.findById(newInfo.getQuizID()).get();
        if (currentUser.getCourses().contains(updatedDeployment.getDeploymentCourse())) {
            if (updatedDeployment.getDeploymentCourse().getCourseID() != courseId) {
                updatedDeployment.setDeploymentCourse(courseRepository.findById(courseId).get());
            }
            for (Question question : newInfo.getQuestions()) {
                question.setQuizID(quizToUpDate.getQuizID());
                if (question.getType() == 1 || question.getType() == 3) {
                    HashSet<Alternative> questionAlternativeSet = new HashSet<>();
                    for (Alternative alternative : question.getAlternatives()) {

                        if (alternative.getAlternativeID() != null && !alternative.getAlternative().isBlank()) {
                            Alternative existingAlternative = alternativeRepository.findById(alternative.getAlternativeID()).orElse(null);
                            if (existingAlternative != null) {
                                existingAlternative.setAlternative(alternative.getAlternative());
                                existingAlternative.setRightAlternative(alternative.isRightAlternative());
                                questionAlternativeSet.add(alternativeRepository.save(existingAlternative));
                            }
                        } else {
                            if (!alternative.getAlternative().isBlank()) {
                                questionAlternativeSet.add(alternativeRepository.save(alternative));
                            }
                        }
                    }
                    question.setAlternatives(questionAlternativeSet);
                } else {
                    question.setAlternatives(new HashSet<>());
                }
                questionRepository.save(question);
            }
            quizToUpDate.setQuestions(newInfo.getQuestions());
            quizRepository.save(quizToUpDate);
            deployedQuizRepository.save(updatedDeployment);
        }

    }


    /**
     * Grades answers to a quiz based
     * on the input of a user with the ADMIN or TEACHER role
     *
     * @param answerIds      list of ids of the answers to grade
     * @param grade          grade to give the answers
     * @param feedback       feedback to give the answers
     * @param deployedQuizId id of the deployedQuiz to grade answers from
     */
    public void gradeQuizzes(List<Integer> answerIds, Double grade, String feedback, Integer deployedQuizId) {
        Optional<DeployedQuiz> deployedQuizOptional = deployedQuizRepository.findById(deployedQuizId);
        DeployedQuiz currentDeployedQuiz = null;
        if (deployedQuizOptional.isPresent()) {
            currentDeployedQuiz = deployedQuizOptional.get();


        }

        for (Integer ansId : answerIds) {
            Optional<QuestionAnswer> questionAnswerOptional = questionAnswerRepository.findById(ansId);
            if (questionAnswerOptional.isPresent()) {
                QuestionAnswer currentQuestionAnswer = questionAnswerOptional.get();
                if (currentQuestionAnswer.getStatus().equals("submitted")) {
                    currentQuestionAnswer.setGrading(grade);
                    currentQuestionAnswer.setFeedback(feedback);
                    currentQuestionAnswer.setStatus("graded");
                    questionAnswerRepository.save(currentQuestionAnswer);
                    for (QuizAnswer qa : currentDeployedQuiz.getQuizAnswer()) {
                        User newNotificationUser = qa.getUser();
                        if (qa.checkAllAnswersGraded() && !qa.isNotified()) {
                            Notification newNotification = new Notification();
                            newNotification.setType("quiz:graded");
                            newNotification.setTitle("Quiz graded");
                            newNotification.setQuizID("" + currentDeployedQuiz.getQuiz().getQuizID());
                            newNotification.setMessage("Your answers to the quiz: " + currentDeployedQuiz.getQuiz().getTitle() + " has been graded");
                            qa.setNotified(true);
                            notificationRepository.save(newNotification);
                            newNotificationUser.addNotification(newNotification);
                            userRepository.save(newNotificationUser);
                            quizAnswerRepository.save(qa);
                        }

                    }
                }
            }
        }

        deployedQuizRepository.save(currentDeployedQuiz);

    }

    /**
     * Retrieves the questionAnswers to send them over websockets
     *
     * @param deployedQuizId id of the deployedQuiz to retrieve the answers from.
     * @return Returns an array containing all submitted and graded questionAnswers in the form of a string.
     */
    @Transactional
    public String getQuestionAnswers(Integer deployedQuizId) {
        Optional<DeployedQuiz> foundDeployedQuiz = deployedQuizRepository.findById(deployedQuizId);
        if (foundDeployedQuiz.isPresent()) {
            DeployedQuiz deployedQuiz = foundDeployedQuiz.get();
            return deployedQuiz.getQuestionAnswers();
        } else {
            throw new NoSuchElementException("That quiz is not found in deployment");
        }

    }
}
