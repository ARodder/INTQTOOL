package dev.roder.INTQTOOLBackend.Services;

import dev.roder.INTQTOOLBackend.Entities.User;
import dev.roder.INTQTOOLBackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addUser(User user){
        userRepository.save(user);
    }

    public ArrayList<String> getAllUsers(){
        ArrayList<String> allUsers = new ArrayList<String>();

        Iterator<User> it = userRepository.findAll().iterator();

        while(it.hasNext()){
            allUsers.add(it.next().toString());
        }

        return allUsers;
    }
}
