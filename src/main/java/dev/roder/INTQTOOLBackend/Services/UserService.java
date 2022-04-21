package dev.roder.INTQTOOLBackend.Services;

import dev.roder.INTQTOOLBackend.Entities.*;
import dev.roder.INTQTOOLBackend.Repositories.*;
import org.apache.tomcat.jni.Local;
import org.json.JSONObject;
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

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private DeployedQuizRepository deployedQuizRepository;

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
            for(DeployedQuiz quiz :course.getActiveQuizzes()){
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

            if(newCourse != null && !currentUser.getCourses().contains(newCourse)){
                currentUser.addCourse(newCourse);
                userRepository.save(currentUser);
                joinSuccess = true;
            }
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
            if(qa.getStatus() != "submitted"){
                return qa.toString();
            }else {
                return "Answers submitted";
            }

        } else {
            return "No answer";
        }


    }

    public boolean saveUserQuizAnswer(QuizAnswer qa,Integer deployementId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        boolean saveSuccess = false;

        try{
            List<QuestionAnswer> savedQuestionAnswers = new ArrayList();
            qa.getAnswers().forEach((ans)->{

                if(ans.getId() != null){
                    QuestionAnswer existingAnswer = questionAnswerRepository.findById(ans.getId()).get();
                    if(!existingAnswer.getStatus().equals("submitted")){
                        existingAnswer.setAnswer(ans.getAnswer());
                        existingAnswer.setStatus(ans.getStatus());
                        savedQuestionAnswers.add(questionAnswerRepository.save(existingAnswer));
                    }
                }else{
                    savedQuestionAnswers.add(questionAnswerRepository.save(ans));
                }

            });

            qa.setAnswers(savedQuestionAnswers);

            if(qa.getId()  != null){
                QuizAnswer existingQuizAnswer = quizAnswerRepository.findById(qa.getId()).get();
                if(!existingQuizAnswer.getStatus().equals("submitted")){
                    existingQuizAnswer.setAnswers(qa.getAnswers());
                    existingQuizAnswer.setStatus(qa.getStatus());
                    quizAnswerRepository.save(existingQuizAnswer);
                }
            } else{
                qa.setUser(currentUser);
                DeployedQuiz currentDeployedQuiz = deployedQuizRepository.findById(deployementId).get();
                if(currentDeployedQuiz.getDeadline().isBefore(LocalDate.now())){
                    if(currentUser.getCourses().contains(currentDeployedQuiz.getDeploymentCourse())){
                        qa.setDeployedQuiz(currentDeployedQuiz);
                    }
                }

                QuizAnswer savedQuizAnswer = quizAnswerRepository.save(qa);
                currentUser.addQuizAnswer(savedQuizAnswer);
                userRepository.save(currentUser);
            }



            saveSuccess = true;
        } catch (Exception e){
            System.out.println(e);
            saveSuccess = false;
        }

        return saveSuccess;
    }

    public boolean submitUserQuizAnswer(QuizAnswer qa, Integer deployementId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();

        boolean saveSuccess = false;

        try{
            List<QuestionAnswer> savedQuestionAnswers = new ArrayList();
            qa.getAnswers().forEach((ans)->{

                if(ans.getId() != null){
                    QuestionAnswer existingAnswer = questionAnswerRepository.findById(ans.getId()).get();
                    if(!existingAnswer.getStatus().equals("submitted")){
                        existingAnswer.setAnswer(ans.getAnswer());
                        existingAnswer.setStatus("submitted");
                        savedQuestionAnswers.add(questionAnswerRepository.save(existingAnswer));
                    }
                }else{
                    ans.setStatus("submitted");
                    savedQuestionAnswers.add(questionAnswerRepository.save(ans));
                }

            });

            qa.setAnswers(savedQuestionAnswers);

            if(qa.getId()  != null){
                QuizAnswer existingQuizAnswer = quizAnswerRepository.findById(qa.getId()).get();
                if(!existingQuizAnswer.getStatus().equals("submitted")){
                    existingQuizAnswer.setAnswers(qa.getAnswers());
                    existingQuizAnswer.setStatus("submitted");
                    quizAnswerRepository.save(existingQuizAnswer);
                }
            } else{
                qa.setUser(currentUser);
                qa.setStatus("submitted");
                DeployedQuiz currentDeployedQuiz = deployedQuizRepository.findById(deployementId).get();
                if(currentDeployedQuiz.getDeadline().isBefore(LocalDate.now())){
                    if(currentUser.getCourses().contains(currentDeployedQuiz.getDeploymentCourse())){
                        qa.setDeployedQuiz(currentDeployedQuiz);
                    }
                }
                QuizAnswer savedQuizAnswer = quizAnswerRepository.save(qa);
                currentUser.addQuizAnswer(savedQuizAnswer);
                userRepository.save(currentUser);
            }



            saveSuccess = true;
        } catch (Exception e){
            System.out.println(e);
            saveSuccess = false;
        }

        return saveSuccess;
    }

    public Iterable<String> getArchivedQuizzes(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentPrincipalName).get();
        return currentUser.getQuizAnswers()
                .stream()
                .filter((quizAnswer)->!quizAnswer.getStatus().equals("in-progress"))
                .map((quizAnswer ->{
                    Quiz q = quizAnswer.getDeployedQuiz().getDepolyedQuiz();
                    JSONObject details = new JSONObject();
                    details.put("id",quizAnswer.getId());
                    details.put("title",q.getTitle());
                    details.put("status",quizAnswer.getStatus());
                    details.put("description",q.getDescription());
                    details.put("quizLength",q.getQuestions().size());
                    return details.toString();
                })).collect(Collectors.toList());
    }
}
