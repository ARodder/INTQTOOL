package dev.roder.INTQTOOLBackend.Controllers;

import dev.roder.INTQTOOLBackend.Repositories.CourseRepository;
import dev.roder.INTQTOOLBackend.Services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping(path="/course")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @RequestMapping(method= RequestMethod.GET, path="/{courseId}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public String getCourse(@PathVariable("courseId") Integer courseId){
        return courseRepository.findById(courseId).get().getDetails();
    }

    @RequestMapping(method= RequestMethod.POST, path="/adduser/{courseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addUserToCourse(@PathVariable("courseId") Integer courseId, @RequestBody Map<String, Integer> json){
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        try{
            courseService.addUserToCourse(courseId, json.get("userId"));
            response = new ResponseEntity<String>(HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return response;
        }
        return response;
    }

 }
