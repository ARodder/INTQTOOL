package dev.roder.intqtoolbackend.Security;

import dev.roder.intqtoolbackend.Entities.User;
import dev.roder.intqtoolbackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Class used by spring when verifying users.
 * implements the UserDetailsService interface.
 * Retrieves a user and converts it to an instance of userDetails
 */
@Service
public class IntqtoolUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    /**
     * Loads a user by their username and converts the user to
     * an instance of the UserDetial
     * @param username username of the user to find
     * @return returns userDetail
     * @throws UsernameNotFoundException throws Exception if the user was not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         Optional<User> user = userRepository.findByUsername(username);
        return new IntqtoolUserDetails(user.get());
    }
}
