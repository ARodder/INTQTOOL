package dev.roder.INTQTOOLBackend;

import java.net.HttpCookie;

public class UserController {

    //Might need to use different cookie class or use String instead
    public boolean authenticate(HttpCookie cookie) {
        return true;
    }

    public User login(String loginDetails) {
        return new User();
    }
}
