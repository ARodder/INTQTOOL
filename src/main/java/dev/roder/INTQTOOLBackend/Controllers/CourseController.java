package dev.roder.INTQTOOLBackend.Controllers;

import dev.roder.INTQTOOLBackend.Entities.Course;
import dev.roder.INTQTOOLBackend.Services.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.Map;

@Controller
// TODO - REST convention is to use plural: /courses
@RequestMapping(path="/course")
public class CourseController {
    Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Autowired
    private CourseService courseService;

    @RequestMapping(method= RequestMethod.GET, path="/{courseId}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getCourse(@PathVariable("courseId") Integer courseId){
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        try{
            // TODO - Run code re-format now and then, make sure all the spacing and indentation is correct. For example, here a space is missing after =
            String foundCourse =courseService.getCourseDetails(courseId);
            response = new ResponseEntity<String>( foundCourse,HttpStatus.OK);
        }catch(Exception e){
            // TODO - don't use System.out.println, use log4j logging like this:
            logger.warn(e.getMessage());
            return response;
        }
        return response;
    }

    // TODO -the usual REST API convention is to return all objects simply at the HTTP GET /course request, no need for /course/all
    @RequestMapping(method= RequestMethod.GET, path="/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllCourses(){
        // TODO - did some refactoring here
        ResponseEntity<?> response;

        try{
            Collection<Course> courses = courseService.getAllCourses();
            response = new ResponseEntity<>(courses, HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e.getMessage());
            response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    // TODO - the usual REST API convention is to create a new object when HTTP POST /courses is sent
    @RequestMapping(method= RequestMethod.POST, path="/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createNewCourse(@RequestBody Course newCourse){
        ResponseEntity<?> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        try{
            courseService.createNewQuiz(newCourse);
            // TODO - should you really return all the courses here? The convention is to simply return ID of the newly created entity
            response = new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e.getMessage());
            // TODO - this return is not needed
            return response;
        }
        return response;
    }

    // TODO - REST API convention would be /courses/{courseId}/adduser
    @RequestMapping(method= RequestMethod.POST, path="/adduser/{courseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addUserToCourse(@PathVariable("courseId") Integer courseId, @RequestBody Map<String, Integer> json){
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        try{
            courseService.addUserToCourse(courseId, json.get("userId"));
            response = new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return response;
        }
        return response;
    }

 }
