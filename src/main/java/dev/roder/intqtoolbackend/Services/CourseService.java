package dev.roder.intqtoolbackend.Services;

import dev.roder.intqtoolbackend.Entities.Course;
import dev.roder.intqtoolbackend.Entities.User;
import dev.roder.intqtoolbackend.Repositories.*;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.*;

/**
 * Service class containing most of the functionality
 * for any endpoint with the "/course/" prefix
 */
@Service
public class CourseService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * Adds a user to a course where user is based on the
     * userId and course is based on the courseId
     * @param courseId id of the course to add the user to.
     * @param userId id of the user to add to a courses.
     */
    public void addUserToCourse(Integer courseId, Integer userId) {
        User userToEdit = userRepository.findById(userId).get();
        Course courseToJoin = courseRepository.findById(courseId).get();
        if (courseToJoin != null && !userToEdit.getCourses().contains(courseToJoin)) {
            userToEdit.addCourse(courseToJoin);
            userRepository.save(userToEdit);

        } else {
            throw new IllegalArgumentException("Course or user does not exist");
        }

    }

    /**
     * Creates a new course
     *
     * @param newCourse Course object containing details of the new course
     * @throws ValidationException Exception thrown if the course is not valid
     */
    public void createNewCourse(Course newCourse) throws ValidationException {
        try {
            newCourse.setActiveQuizzes(new ArrayList<>());
            newCourse.setJoinCode(generateJoinCode());
            courseRepository.save(newCourse);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new ValidationException("Not valid course object");
        }

    }

    /**
     * Retrieves details of a course specified by it's id
     *
     * @param courseId id of the course to find
     * @return Returns the details of the found course
     */
    public String getCourseDetails(Integer courseId) {
        Optional<Course> foundCourse = courseRepository.findById(courseId);
        if (foundCourse.isPresent()) {
            return foundCourse.get().getDetails();
        } else {
            throw new NoSuchElementException("Course does not exist");
        }

    }

    /**
     * Retrieves the details of all courses in the database
     *
     * @return Returns a list of all courses
     */
    public String getAllCourseDetails() {
        JSONArray allCourses = new JSONArray();
        Iterator<Course> it = courseRepository.findAll().iterator();
        while (it.hasNext()) {
            allCourses.put(it.next().getDetails());
        }

        return allCourses.toString();
    }

    /**
     * Generates a pseudo-random joinCode for a new course
     * with all upperCase letters or numbers(excluding certain special characters from utf-8
     * with index greater than 57 but less than 65, and index greater than 90 but less than 97).
     *
     * @return Returns the pseudo-randomly generated string
     */
    private String generateJoinCode() {
        String newJoinCode = "";
        Random random = new Random();
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;

        do{
            newJoinCode = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            newJoinCode = newJoinCode.toUpperCase();

        }while(courseRepository.findByJoinCode(newJoinCode).isPresent());

        return newJoinCode;
    }
}
