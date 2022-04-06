package dev.roder.INTQTOOLBackend.Controllers;

import dev.roder.INTQTOOLBackend.Entities.Course;
import dev.roder.INTQTOOLBackend.Entities.User;
import dev.roder.INTQTOOLBackend.Repositories.UserRepository;
import dev.roder.INTQTOOLBackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
    public ResponseEntity<String> UserController(@RequestBody User user){

        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(user.isValid()){
            userService.addUser(user);
            response = new ResponseEntity<>(HttpStatus.CREATED);
        }

        return response;

    }

    @GetMapping(path="/all")
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
    public ResponseEntity<String> joinCourse(@PathVariable("joinCode") String joinCode){

        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userService.joinCourse(joinCode)) {
            response = new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        return response;

    }

    @RequestMapping(method=RequestMethod.GET, path="/clearnotifications")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> clearNotifications(){
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userService.clearNotifications()){
            response = new ResponseEntity<>(HttpStatus.OK);
        }

        return response;

    }

    @RequestMapping(method=RequestMethod.GET, path="/removenotification/{notificationID}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> removeNotification(@PathVariable("notificationID") String notificationID){
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userService.removeNotification(notificationID)){
            response = new ResponseEntity<>(HttpStatus.OK);
        }

        return response;

    }





}
