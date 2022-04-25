package dev.roder.INTQTOOLBackend.Controllers;

import dev.roder.INTQTOOLBackend.Entities.Course;
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
    private CourseService courseService;

    @RequestMapping(method= RequestMethod.GET, path="/{courseId}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getCourse(@PathVariable("courseId") Integer courseId){
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        try{
            String foundCourse =courseService.getCourseDetails(courseId);
            response = new ResponseEntity<String>( foundCourse,HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return response;
        }
        return response;
    }

    @RequestMapping(method= RequestMethod.GET, path="/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getAllCourses(){
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        try{
            String foundCourses = courseService.getAllCourseDetails();
            response = new ResponseEntity<String>( foundCourses,HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return response;
        }
        return response;
    }

    @RequestMapping(method= RequestMethod.POST, path="/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createNewCourse(@RequestBody Course newCourse){
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        try{
            String foundCourses = courseService.createNewQuiz(newCourse);
            response = new ResponseEntity<String>( foundCourses,HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return response;
        }
        return response;
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
