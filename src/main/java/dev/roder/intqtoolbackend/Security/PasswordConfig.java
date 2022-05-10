package dev.roder.intqtoolbackend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring configuration to configure the password encryption type
 */
@Configuration
public class PasswordConfig {

    /**
     * Creates passwordEncoder used to encode the passwords in the databse and when logging in
     * @return Returns the used encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder(10);}
}
