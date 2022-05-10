package dev.roder.intqtoolbackend.Services;

import dev.roder.intqtoolbackend.Entities.*;
import dev.roder.intqtoolbackend.Repositories.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class containing most of the functionality
 * for any endpoint with the "/user/" prefix
 */
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
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private DeployedQuizRepository deployedQuizRepository;
    @Autowired
    private QuizService quizService;
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Adds a new user to the database
     * with certain pre-determined values.
     *
     * @param user new user to add
     */
    public void addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isEmpty() && userRepository.findByEmail(user.getEmail()).isEmpty()) {
            Role role = new Role("ROLE_STUDENT");

            user.setSettings("0,0,0,0,0");
            user.addRole(role);
            user.setExpirationDate(LocalDate.now().plusYears(5));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setAccountLock(false);
            user.setUserEnabled(true);
            roleRepository.save(role);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Username already in use");
        }

    }

    /**
     * Retrieves a list of all users in the database
     *
     * @return Returns list of users
     */
    public ArrayList<String> getAllUsers() {
        ArrayList<String> allUsers = new ArrayList<String>();

        Iterator<User> it = userRepository.findAll().iterator();

        while (it.hasNext()) {
            allUsers.add(it.next().getDetails());
        }

        return allUsers;
    }

    /**
     * Retrieves the current user based on the security context.
     *
     * @return Returns current user.
     */
    public String getUser() {
        return getCurrentUser().toString();
    }

    /**
     * Retrieves the active quizzes of a user
     *
     * @return Returns a list of deployedQuizzes in the form of strings
     */
    public List<String> getUsersActiveQuizes() {
        List<String> quizzes = new ArrayList<String>();
        User currentUser = getCurrentUser();

        for (Course course : currentUser.getCourses()) {
            for (DeployedQuiz quiz : course.getActiveQuizzes()) {
                quizzes.add(quiz.getDetails());
            }
        }

        return quizzes;
    }

    /**
     * Retrieves a list of the courses the user is in.
     *
     * @return Returns a list of course details in the form of a string
     */
    public List<String> getUsersCourses() {
        List<String> courses = new ArrayList<String>();

        User currentUser = getCurrentUser();

        for (Course course : currentUser.getCourses()) {
            courses.add(course.getDetails());
        }

        return courses;
    }

    /**
     * Retrieves a list of notifications for the current user.
     *
     * @return Returns list of notification details in the form of a string.
     */
    public List<String> getNotifications() {
        List<String> notifications = new ArrayList<String>();

        User currentUser = getCurrentUser();

        for (Notification notification : currentUser.getNotifications()) {
            notifications.add(notification.getDetails());
        }

        return notifications;
    }

    /**
     * Join course using randomly generated joinCode.
     *
     * @param joinCode Code used to join the course.
     * @return return boolean based on if the join was successful.
     */
    public boolean joinCourse(String joinCode) {

        User currentUser = getCurrentUser();
        boolean joinSuccess = false;


        try {
            Course newCourse = courseRepository.findByJoinCode(joinCode).get();

            if (newCourse != null && !currentUser.getCourses().contains(newCourse)) {
                currentUser.addCourse(newCourse);
                userRepository.save(currentUser);
                joinSuccess = true;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            joinSuccess = false;
        }

        return joinSuccess;
    }

    /**
     * Clears a users notification list.
     *
     * @return Returns boolean  depending on if the clearing was successful
     */
    public boolean clearNotifications() {
        User currentUser = getCurrentUser();

        boolean clearSuccess = false;

        try {
            List<Notification> currentUserNotifications = new ArrayList<>(currentUser.getNotifications());


            currentUser.setNotifications(new ArrayList<>());

            userRepository.save(currentUser);

            for (Notification notification : currentUserNotifications) {
                notificationRepository.delete(notification);
            }
            clearSuccess = true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return clearSuccess;
        }

        return clearSuccess;
    }

    /**
     * Removes a specific notification from a users notification list.
     *
     * @param notificationID id of the notification to remove
     * @return returns boolean depending on if the deletion was a success.
     */
    public boolean removeNotification(Integer notificationID) {

        User currentUser = getCurrentUser();

        boolean delSuccess = false;

        try {
            List<Integer> notificationIDs = currentUser.getNotifications().stream().map(Notification::getNotificationID).collect(Collectors.toList());

            if (notificationIDs.contains(notificationID)) {
                currentUser.removeNotification(notificationID);
                notificationRepository.delete(notificationRepository.findByNotificationID(notificationID).get());
                delSuccess = true;
            }

        } catch (Exception e) {
            logger.warn(e.getMessage());
            delSuccess = false;
        }

        return delSuccess;
    }

    /**
     * Retrieves the currentUsers quizAnswers for a specified quizID
     * if the quiz-answer status is "in-progress".
     *
     * @param quizID id of the quiz to retrieve answers for.
     * @return Returns the QuizAnswer Object in the form of a string
     */
    public String getUserQuizAnswers(Integer quizID) {
        User currentUser = getCurrentUser();

        QuizAnswer qa = currentUser.getQuizAnswers(quizID);

        if (qa != null) {
            if (qa.getStatus().equals("in-progress")) {
                return qa.toString();
            } else {
                return "Answers submitted";
            }

        } else {
            return "No answer";
        }


    }

    /**
     * Saves the current users answer to a quiz without submitting the answers
     *
     * @param qa quizAnswers to save
     * @param deployementId id of the deploymentQuiz to save them to
     * @return Returns boolean depending on if the save was a success.
     */
    public boolean saveUserQuizAnswer(QuizAnswer qa, Integer deployementId) {

        User currentUser = getCurrentUser();

        boolean saveSuccess = false;

        try {
            List<QuestionAnswer> savedQuestionAnswers = new ArrayList();
            qa.getAnswers().forEach((ans) -> {

                if (ans.getId() != null) {
                    QuestionAnswer existingAnswer = questionAnswerRepository.findById(ans.getId()).orElse(null);
                    if (existingAnswer != null && existingAnswer.getStatus().equals("in-progress")) {
                        existingAnswer.setAnswer(ans.getAnswer());
                        existingAnswer.setStatus(ans.getStatus());
                        savedQuestionAnswers.add(questionAnswerRepository.save(existingAnswer));
                    }
                } else {
                    savedQuestionAnswers.add(questionAnswerRepository.save(ans));
                }

            });

            qa.setAnswers(savedQuestionAnswers);

            if (qa.getId() != null) {
                QuizAnswer existingQuizAnswer = quizAnswerRepository.findById(qa.getId()).orElse(null);
                if (existingQuizAnswer != null && existingQuizAnswer.getStatus().equals("in-progress")) {
                    existingQuizAnswer.setAnswers(qa.getAnswers());
                    existingQuizAnswer.setStatus(qa.getStatus());
                    quizAnswerRepository.save(existingQuizAnswer);
                }
            } else {
                qa.setUser(currentUser);
                DeployedQuiz currentDeployedQuiz = deployedQuizRepository.findById(deployementId).orElse(null);
                if (currentDeployedQuiz != null && currentDeployedQuiz.getDeadline().compareTo(new Date()) > 0) {
                    if (currentUser.getCourses().contains(currentDeployedQuiz.getDeploymentCourse())) {
                        qa.setDeployedQuiz(currentDeployedQuiz);
                        currentDeployedQuiz.addQuizAnswer(qa);
                    }
                }

                QuizAnswer savedQuizAnswer = quizAnswerRepository.save(qa);
                deployedQuizRepository.save(currentDeployedQuiz);
                currentUser.addQuizAnswer(savedQuizAnswer);
                userRepository.save(currentUser);
            }


            saveSuccess = true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            saveSuccess = false;
        }

        return saveSuccess;
    }

    /**
     * Submits a users answer to a quiz.
     *
     * @param qa quizAnswers to save
     * @param deployementId id of the deployment to save the quizAnswer to
     * @return Returns the saved quizAnswer
     */
    public QuizAnswer submitUserQuizAnswer(QuizAnswer qa, Integer deployementId) {
        User currentUser = getCurrentUser();

        boolean saveSuccess = false;

        try {
            List<QuestionAnswer> savedQuestionAnswers = new ArrayList();
            qa.getAnswers().forEach((ans) -> {
                if (ans.getId() != null) {
                    QuestionAnswer existingAnswer = questionAnswerRepository.findById(ans.getId()).orElse(null);
                    if (existingAnswer != null && !existingAnswer.getStatus().equals("submitted")) {
                        existingAnswer.setAnswer(ans.getAnswer());
                        existingAnswer.setStatus("submitted");

                        Optional<Question> currentQuestionOptional = questionRepository.findById(existingAnswer.getQuestionId());
                        if (currentQuestionOptional.isPresent()) {
                            Question currentQuestion = currentQuestionOptional.get();
                            if (currentQuestion.getType() == 1) {
                                existingAnswer.setGrading(currentQuestion.autoGrade(existingAnswer.getAnswer()));
                                existingAnswer.setStatus("graded");
                            }
                        }

                        savedQuestionAnswers.add(questionAnswerRepository.save(existingAnswer));
                    }
                } else {
                    ans.setStatus("submitted");

                    Optional<Question> currentQuestionOptional = questionRepository.findById(ans.getQuestionId());
                    if (currentQuestionOptional.isPresent()) {
                        Question currentQuestion = currentQuestionOptional.get();
                        if (currentQuestion.getType() == 1 || currentQuestion.getType() == 3 ) {
                            ans.setGrading(currentQuestion.autoGrade(ans.getAnswer()));
                            ans.setStatus("graded");
                        }

                        savedQuestionAnswers.add(questionAnswerRepository.save(ans));
                    }

                }

            });

            qa.setAnswers(savedQuestionAnswers);

            if (qa.getId() != null) {
                QuizAnswer existingQuizAnswer = quizAnswerRepository.findById(qa.getId()).orElse(null);
                if (existingQuizAnswer != null && !existingQuizAnswer.getStatus().equals("submitted")) {
                    existingQuizAnswer.setAnswers(qa.getAnswers());
                    existingQuizAnswer.setStatus("submitted");


                    quizAnswerRepository.save(existingQuizAnswer);
                }
                quizService.getQuestionAnswers(deployementId);
                return existingQuizAnswer;
            } else {
                qa.setUser(currentUser);
                qa.setStatus("submitted");
                qa.setSubmittedDate(new Timestamp(System.currentTimeMillis()));
                DeployedQuiz currentDeployedQuiz = deployedQuizRepository.findById(deployementId).orElse(null);
                if (currentDeployedQuiz != null && currentDeployedQuiz.getDeadline().compareTo(new Date()) > 0) {
                    if (currentUser.getCourses().contains(currentDeployedQuiz.getDeploymentCourse())) {
                        qa.setDeployedQuiz(currentDeployedQuiz);
                        currentDeployedQuiz.addQuizAnswer(qa);
                    }
                }
                qa.checkAllAnswersGraded();
                QuizAnswer savedQuizAnswer = quizAnswerRepository.save(qa);

                deployedQuizRepository.save(currentDeployedQuiz);
                currentUser.addQuizAnswer(savedQuizAnswer);
                userRepository.save(currentUser);

                //quizService.getQuestionAnswers(deployementId);
                return savedQuizAnswer;
            }


        } catch (Exception e) {
            logger.warn(e.getMessage());
            return null;
        }


    }

    /**
     * Retrieves the current user based on the security context
     *
     * @return Returns user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(currentPrincipalName);
        return userOptional.orElse(null);

    }

    /**
     * Retrieves a list of the archived quizzes of the current user
     *
     * @return Returns list of submitted or graded QuizAnswers.
     */
    public Iterable<String> getArchivedQuizzes() {
        User currentUser = getCurrentUser();
        return currentUser.getQuizAnswers()
                .stream()
                .filter((quizAnswer) -> !quizAnswer.getStatus().equals("in-progress"))
                .map((quizAnswer -> {
                    try {
                        Quiz q = quizAnswer.getDeployedQuiz().getQuiz();
                        JSONObject details = new JSONObject();
                        details.put("id", quizAnswer.getId());
                        details.put("quizId", quizAnswer.getDeployedQuiz().getQuiz().getQuizID());
                        details.put("title", q.getTitle());
                        details.put("status", quizAnswer.getStatus());
                        details.put("description", q.getDescription());
                        details.put("grading", quizAnswer.getGrading());
                        details.put("quizLength", q.getQuestions().size());
                        return details.toString();
                    } catch (NullPointerException e) {
                        logger.warn(e.getMessage());

                        return null;
                    }
                })).filter((qa) -> qa != null).collect(Collectors.toList());

    }

    /**
     * Changes the role of a user to a student.
     *
     * @param userId id of the user to change role
     */
    public void makeUserStudent(Integer userId) {
        User userToChange = userRepository.findById(userId).orElse(null);
        if (userToChange != null && !userToChange.hasRole("ROLE_STUDENT")) {
            for (Role role : userToChange.getRoles()) {
                userToChange.removeRole(role);
                roleRepository.delete(role);
            }
            Role newStudentRole = new Role("ROLE_STUDENT");
            roleRepository.save(newStudentRole);
            userToChange.addRole(newStudentRole);
            userRepository.save(userToChange);
        }

    }

    /**
     * Changes the role of a user to a teacher.
     *
     * @param userId id of the user to change role
     */
    public void makeUserTeacher(Integer userId) {
        User userToChange = userRepository.findById(userId).orElse(null);
        if (userToChange != null && !userToChange.hasRole("ROLE_TEACHER")) {
            for (Role role : userToChange.getRoles()) {
                userToChange.removeRole(role);
                roleRepository.delete(role);
            }
            Role newStudentRole = new Role("ROLE_TEACHER");
            roleRepository.save(newStudentRole);
            userToChange.addRole(newStudentRole);
            userRepository.save(userToChange);
        }

    }

    /**
     * Changes the role of a user to a admin.
     *
     * @param userId id of the user to change role
     */
    public void makeUserAdmin(Integer userId) {
        User userToChange = userRepository.findById(userId).orElse(null);
        if (userToChange != null && !userToChange.hasRole("ROLE_ADMIN")) {
            for (Role role : userToChange.getRoles()) {
                userToChange.removeRole(role);
                roleRepository.delete(role);
            }
            Role newStudentRole = new Role("ROLE_ADMIN");
            roleRepository.save(newStudentRole);
            userToChange.addRole(newStudentRole);
            userRepository.save(userToChange);
        }

    }

    /**
     * Retrieves one of the current user's QuizAnswers
     * based on the answerId.
     *
     * @param answerId id of the answer to retrieve.
     * @return Returns the answer in the form of a string.
     */
    public String getAnsweredQuiz(Integer answerId) {
        User currentUser = getCurrentUser();
        Optional<QuizAnswer> quizAnswerOptional = quizAnswerRepository.findById(answerId);

        if (quizAnswerOptional.isPresent()) {
            QuizAnswer quizAnswer = quizAnswerOptional.get();
            if (currentUser.getQuizAnswers().contains(quizAnswer)) {
                if (quizAnswer.getStatus().equals("submitted") || quizAnswer.getStatus().equals("graded")) {
                    return quizAnswer.getAnswerDetails();
                }
            }
        }

        return null;


    }
}
