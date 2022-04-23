package dev.roder.INTQTOOLBackend.Services;

import dev.roder.INTQTOOLBackend.Entities.Course;
import dev.roder.INTQTOOLBackend.Entities.User;
import dev.roder.INTQTOOLBackend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public void addUserToCourse(Integer courseId,Integer userId){
        User userToEdit = userRepository.findById(userId).get();
        Course courseToJoin = courseRepository.findById(courseId).get();
        if(courseToJoin != null && !userToEdit.getCourses().contains(courseToJoin)){
            userToEdit.addCourse(courseToJoin);
            userRepository.save(userToEdit);

        }else{
            throw new IllegalArgumentException("Course or user does not exist");
        }

    }
}
