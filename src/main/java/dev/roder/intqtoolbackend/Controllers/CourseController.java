package dev.roder.intqtoolbackend.Controllers;

import dev.roder.intqtoolbackend.Entities.Course;
import dev.roder.intqtoolbackend.Services.CourseService;
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

import java.util.Map;

/**
 * This class defines endpoints relating specifically to courses such as creating a new course,
 * getting a list of all the courses, getting details about a specific course or adding a user to a course.
 */

@Controller
@RequestMapping(path = "/course")
public class CourseController {

    @Autowired
    private CourseService courseService;
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * Defines the endpoint for retrieving the details of a single course given the courseId.
     * Relays the task to the appropriate courseService method.
     * This endpoint is accessible for any of the three roles.
     *
     * @param courseId Id of the course to retrieve details from
     * @return details retrieved for the course id
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{courseId}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getCourse(@PathVariable("courseId") Integer courseId) {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        try {
            String foundCourse = courseService.getCourseDetails(courseId);
            response = new ResponseEntity<String>(foundCourse, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return response;
        }
        return response;
    }

    /**
     * Defines the endpoint for retrieving the details of all courses currently in the database.
     * Relays the task to the appropriate courseService method.
     * This endpoint is accessible for only for users with ROLE_ADMIN
     *
     * @return details for every course in the database.
     */

    @RequestMapping(method = RequestMethod.GET, path = "/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getAllCourses() {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        try {
            String foundCourses = courseService.getAllCourseDetails();
            response = new ResponseEntity<String>(foundCourses, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return response;
        }
        return response;
    }

    /**
     * Defines the endpoint for creating a new course given the required details.
     * Relays the task to the appropriate courseService method.
     * This endpoint is accessible only for users with ROLE_ADMIN.
     *
     * @param newCourse contains details required for creating a new course.
     * @return updated list of all courses in the database after the new course was inserted.
     */

    @RequestMapping(method = RequestMethod.POST, path = "/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createNewCourse(@RequestBody Course newCourse) {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        try {
            courseService.createNewQuiz(newCourse);
            String foundCourses = courseService.getAllCourseDetails();
            response = new ResponseEntity<String>(foundCourses, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return response;
        }
        return response;
    }


    /**
     * Defines an endpoint for adding users to a courses from the administrator tool.
     * Relays the task to the appropriate courseService method.
     * This endpoint is accessible only for users with ROLE_ADMIN.
     *
     * @param courseId the course id of the course to add a user to.
     * @param json used to extract userId from the http requestbody.
     * @return Status based on if the operation was successful or not(200 or 400)
     */

    @RequestMapping(method = RequestMethod.POST, path = "/adduser/{courseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addUserToCourse(@PathVariable("courseId") Integer courseId, @RequestBody Map<String, Integer> json) {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        try {
            courseService.addUserToCourse(courseId, json.get("userId"));
            response = new ResponseEntity<String>(HttpStatus.OK);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return response;
        }
        return response;
    }

}
