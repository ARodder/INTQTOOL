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

    private String currentUserName;


    @PostMapping("/add")
    @PermitAll
    public ResponseEntity<String> UserController(@RequestBody User user){
        currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();

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

    @GetMapping(path="/myquizzes")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER')")
    public void getUsersActiveQuizes(){
        userService.getUsersActiveQuizes();

    }



}
