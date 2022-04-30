package dev.roder.intqtoolbackend.Security;

import dev.roder.intqtoolbackend.Entities.User;
import dev.roder.intqtoolbackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IntqtoolUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         Optional<User> user = userRepository.findByUsername(username);
        return new IntqtoolUserDetails(user.get());
    }
}
