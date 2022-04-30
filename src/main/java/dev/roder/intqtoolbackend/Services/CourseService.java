package dev.roder.intqtoolbackend.Services;

import dev.roder.intqtoolbackend.Entities.Course;
import dev.roder.intqtoolbackend.Entities.User;
import dev.roder.intqtoolbackend.Repositories.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.*;

@Service
public class CourseService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

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

    public void createNewQuiz(Course newCourse) throws ValidationException {
        try {
            newCourse.setActiveQuizzes(new ArrayList<>());
            newCourse.setJoinCode(generateJoinCode());
            courseRepository.save(newCourse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ValidationException("Not valid course object");
        }

    }

    public String getCourseDetails(Integer courseId) {
        Optional<Course> foundCourse = courseRepository.findById(courseId);
        if (foundCourse.isPresent()) {
            return foundCourse.get().getDetails();
        } else {
            throw new NoSuchElementException("Course does not exist");
        }

    }

    public String getAllCourseDetails() {
        JSONArray allCourses = new JSONArray();
        Iterator<Course> it = courseRepository.findAll().iterator();
        while (it.hasNext()) {
            allCourses.put(it.next().getDetails());
        }

        return allCourses.toString();
    }

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
