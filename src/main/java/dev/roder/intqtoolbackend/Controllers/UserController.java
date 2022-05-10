package dev.roder.intqtoolbackend.Controllers;

import dev.roder.intqtoolbackend.Entities.QuizAnswer;
import dev.roder.intqtoolbackend.Entities.User;
import dev.roder.intqtoolbackend.Services.QuizService;
import dev.roder.intqtoolbackend.Services.UserService;
import dev.roder.intqtoolbackend.Services.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());


    /**
     * Endpoint to change a users Role to student.
     * Only Accessible from user with ROLE_ADMIN.
     *
     * @param userId id of the user to give the role of student.
     * @return Returns empty responseEntity stating if the change was successful
     */
    @GetMapping(path="/makestudent/{userid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> makeUserStudent(@PathVariable("userid") Integer userId) {
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            userService.makeUserStudent(userId);
            response = new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return response;
        }
        return response;
    }

    /**
     * Endpoint to change a users Role to teacher.
     * Only Accessible from user with ROLE_ADMIN.
     *
     * @param userId id of the user to give the role of teacher.
     * @return Returns empty responseEntity stating if the change was successful
     */
    @GetMapping(path="/maketeacher/{userid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> makeUserTeacher(@PathVariable("userid") Integer userId) {
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            userService.makeUserTeacher(userId);
            response = new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return response;
        }
        return response;
    }

    /**
     * Endpoint to change a users Role to admin.
     * Only Accessible from user with ROLE_ADMIN.
     *
     * @param userId id of the user to give the role of admin.
     * @return Returns empty responseEntity stating if the change was successful
     */
    @GetMapping(path="/makeadmin/{userid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> makeUserAdmin(@PathVariable("userid") Integer userId) {
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            userService.makeUserAdmin(userId);
            response = new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return response;
        }
        return response;
    }


    /**
     * Endpoint to retrieve user information based on security context.
     * Accessible to any role.
     *
     * @return the user information of the retrieved user
     */
    @GetMapping(path="/myuser")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody String getUser() {
        return userService.getUser();
    }

    /**
     * Endpoint for retrieving a users notifications based on the security context.
     * Accessible to users of any role
     *
     * @return Returns notifications of the current security contest user.
     */
    @GetMapping(path="/notification")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody Iterable<String> getNotifications() {

        return userService.getNotifications();
    }

    /**
     * Endpoint for creating a new user.
     * Accessible without authentication to allow anyone to create an account
     *
     * @param user Object containing the details of the new user
     * @return Returns empty responseEntity with statusCode changing based on the success of the creation.
     */
    @PostMapping("/add")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> createNewUser(@RequestBody User user){

        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try{
            if(user.isValid()){
                userService.addUser(user);
                response = new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                response = new ResponseEntity<>("User not valid",HttpStatus.BAD_REQUEST);
            }
        }catch(IllegalArgumentException e){
            response = new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
            logger.warn(e.getMessage());
        }catch(Exception e){
            response = new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
            logger.warn("Create new user exception");
            logger.warn(e.getMessage());
        }


        return response;

    }

    /**
     * Endpoint for retrieving all users in the database.
     * Accessible only for users with ROLE_ADMIN
     *
     * @return List of all users in the database.
     */

    @GetMapping(path="/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public @ResponseBody Iterable<String> getAllUsers() {
        // This returns a JSON or XML with the users

        return userService.getAllUsers();
    }

    /**
     * Endpoint to retrieve the active quizzes of a user using the security context
     * Accessible from users with any role.
     *
     * @return Returns a list of all the active quizzes the user has.
     */
    @RequestMapping(method=RequestMethod.GET, path="/quizzes")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody Iterable<String> getUsersActiveQuizes(){
        return userService.getUsersActiveQuizes();

    }

    /**
     * Endpoint used to retrieve a list of the courses a user is a part of.
     * Accessible from users with any role.
     *
     * @return Returns a list of all courses the user is a part of.
     */
    @RequestMapping(method=RequestMethod.GET, path="/courses")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody Iterable<String> getUsersCourses(){
        return userService.getUsersCourses();

    }

    /**
     * Endpoint used by user to join a new course based on a randomly generated join-code.
     * Accessible for users with any role.
     *
     * @param joinCode Random string input by the user to join a course.
     * @return Returns a users updated courses after joining a new course.
     */

    @RequestMapping(method=RequestMethod.GET, path="/joincourse/{joinCode}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<Iterable<String>> joinCourse(@PathVariable("joinCode") String joinCode){

        ResponseEntity<Iterable<String>> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userService.joinCourse(joinCode)) {
            response = new ResponseEntity<Iterable<String>>(userService.getUsersCourses(),HttpStatus.ACCEPTED);
        }

        return response;

    }

    /**
     * Endpoint used to clear a users notifications based on the user in the security context.
     * Accessible for users with any role.
     *
     * @return Return response entity with statusCode based on the success of the clearing.
     */

    @RequestMapping(method=RequestMethod.GET, path="/clearnotifications")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<String> clearNotifications(){
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userService.clearNotifications()){
            response = new ResponseEntity<>(HttpStatus.OK);
        }

        return response;

    }

    /**
     * Endpoint used to remove a users specific notification based on the notificationId
     * Accessible for users with any role.
     *
     * @param notificationID Id of the notification to remove.
     * @return Return response entity with statusCode based on the success of the removing.
     */
    @RequestMapping(method=RequestMethod.GET, path="/removenotification/{notificationID}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<String> removeNotification(@PathVariable("notificationID") Integer notificationID){
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userService.removeNotification(notificationID)){
            response = new ResponseEntity<>(HttpStatus.OK);
        }

        return response;

    }

    /**
     * Endpoint to retrieve answers for a specific quiz that is not yet submitted.
     * Accessible for users with any role.
     *
     * @param quizID id of the quiz to find answers for.
     * @return un-submitted answers to of the quiz with the corresponding id.
     */

    @RequestMapping(method=RequestMethod.GET, path="/quizanswers/{quizID}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<String> getUserQuizAnswers(@PathVariable("quizID") Integer quizID){

        return new ResponseEntity<String>(userService.getUserQuizAnswers(quizID),HttpStatus.OK);

    }

    /**
     * Endpoint to save answers for a quiz in-progress without submitting it.
     * Accessible for users of any role.
     *
     * @param qa The instance of quizAnswer to save.
     * @param deploymentId which deployment of the quiz to save the answer to.
     * @return Return statusCode depending on if it was saved successfully.
     */

    @RequestMapping(method=RequestMethod.POST, path="/saveanswer/{deploymentId}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<String> saveUserQuizAnswer(@RequestBody QuizAnswer qa,@PathVariable("deploymentId") Integer deploymentId){
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        if(userService.saveUserQuizAnswer(qa,deploymentId)){
            response = new ResponseEntity<String>(HttpStatus.OK);
        }


        return response;

    }


    /**
     * Endpoint to retrieve all archived quizzes of the user sending the request
     * Accessible for users with any role.
     *
     * @return Returns a ist of archived quizzes
     */
    @RequestMapping(method=RequestMethod.GET, path="/archivedquizzes")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<Iterable<String>> getArchivedQuizzes(){
        ResponseEntity<Iterable<String>> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        try {
            response = new ResponseEntity<>(userService.getArchivedQuizzes(), HttpStatus.OK);
        }catch(Exception e){
            return response;
        }


        return response;

    }

    /**
     * Endpoint to retrieve details of a users quiz answer
     * Accessible for users with any role
     *
     * @param answerId Id of the quizAnswer to retrieve
     * @return Returns the answerQuiz object
     */
    @RequestMapping(method=RequestMethod.GET,path="/answeredquiz/{answerId}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getAnsweredQuiz(@PathVariable("answerId") Integer answerId){
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        try {
            response = new ResponseEntity<String>(userService.getAnsweredQuiz(answerId), HttpStatus.OK);
        }catch(Exception e){
            return response;
        }


        return response;
    }





}
