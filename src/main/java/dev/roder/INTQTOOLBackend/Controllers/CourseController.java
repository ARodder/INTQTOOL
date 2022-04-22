package dev.roder.INTQTOOLBackend.Controllers;

import dev.roder.INTQTOOLBackend.Repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path="/course")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @RequestMapping(method= RequestMethod.GET, path="/{courseId}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public String getCourse(@PathVariable("courseId") Integer courseId){
        return courseRepository.findById(courseId).get().getDetails();
    }

 }
