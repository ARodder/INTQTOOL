package dev.roder.INTQTOOLBackend.Controllers;

import dev.roder.INTQTOOLBackend.Entities.Course;
import dev.roder.INTQTOOLBackend.Entities.User;
import dev.roder.INTQTOOLBackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.HttpCookie;
import java.util.List;

@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<String> UserController(@RequestBody User user){
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(user.isValid()){
            userRepository.save(user);
            response = new ResponseEntity<>(HttpStatus.CREATED);
        }

        return response;

    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }



}
