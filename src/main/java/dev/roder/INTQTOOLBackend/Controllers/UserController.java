package dev.roder.INTQTOOLBackend.Controllers;

import dev.roder.INTQTOOLBackend.Entities.QuizAnswer;
import dev.roder.INTQTOOLBackend.Entities.User;
import dev.roder.INTQTOOLBackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserService userService;



    @GetMapping(path="/myuser")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody String getUser() {
        // This returns a JSON or XML with the users


        return userService.getUser();
    }

    @GetMapping(path="/notification")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody Iterable<String> getNotifications() {
        // This returns a JSON or XML with the users


        return userService.getNotifications();
    }

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
        }catch(Exception e){
            response = new ResponseEntity<>("Error was thrown",HttpStatus.BAD_REQUEST);
            System.out.println("Create new user exception");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }


        return response;

    }

    @GetMapping(path="/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public @ResponseBody Iterable<String> getAllUsers() {
        // This returns a JSON or XML with the users

        return userService.getAllUsers();
    }

    @RequestMapping(method=RequestMethod.GET, path="/quizzes")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody Iterable<String> getUsersActiveQuizes(){
        return userService.getUsersActiveQuizes();

    }

    @RequestMapping(method=RequestMethod.GET, path="/courses")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody Iterable<String> getUsersCourses(){
        return userService.getUsersCourses();

    }

    @RequestMapping(method=RequestMethod.GET, path="/joincourse/{joinCode}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<Iterable<String>> joinCourse(@PathVariable("joinCode") String joinCode){

        ResponseEntity<Iterable<String>> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userService.joinCourse(joinCode)) {
            response = new ResponseEntity<Iterable<String>>(userService.getUsersCourses(),HttpStatus.ACCEPTED);
        }

        return response;

    }

    @RequestMapping(method=RequestMethod.GET, path="/clearnotifications")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<String> clearNotifications(){
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userService.clearNotifications()){
            response = new ResponseEntity<>(HttpStatus.OK);
        }

        return response;

    }

    @RequestMapping(method=RequestMethod.GET, path="/removenotification/{notificationID}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<String> removeNotification(@PathVariable("notificationID") Integer notificationID){
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userService.removeNotification(notificationID)){
            response = new ResponseEntity<>(HttpStatus.OK);
        }

        return response;

    }

    @RequestMapping(method=RequestMethod.GET, path="/quizanswers/{quizID}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<String> getUserQuizAnswers(@PathVariable("quizID") Integer quizID){

        return new ResponseEntity<String>(userService.getUserQuizAnswers(quizID),HttpStatus.OK);

    }

    @RequestMapping(method=RequestMethod.POST, path="/saveanswer/{deploymentId}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<String> saveUserQuizAnswer(@RequestBody QuizAnswer qa,@PathVariable("deploymentId") Integer deploymentId){
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        if(userService.saveUserQuizAnswer(qa,deploymentId)){
            response = new ResponseEntity<String>(userService.getUserQuizAnswers(qa.getDeployedQuiz().getDeployedQuiz().getQuizID()),HttpStatus.OK);
        }


        return response;

    }

    @RequestMapping(method=RequestMethod.POST, path="/submitanswer/{deploymentId}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<String> submitUserQuiz(@RequestBody QuizAnswer qa,@PathVariable("deploymentId") Integer deploymentId){
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        if(userService.submitUserQuizAnswer(qa,deploymentId)){
            response = new ResponseEntity<>(HttpStatus.OK);
        }

        return response;

    }

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





}
