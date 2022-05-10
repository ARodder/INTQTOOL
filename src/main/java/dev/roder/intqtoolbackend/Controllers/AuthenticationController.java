package dev.roder.intqtoolbackend.Controllers;

import dev.roder.intqtoolbackend.Security.IntqtoolUserDetailService;
import dev.roder.intqtoolbackend.Security.jwt.AuthenticationRequest;
import dev.roder.intqtoolbackend.Security.jwt.AuthenticationResponse;
import dev.roder.intqtoolbackend.Security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is a controller identifying an endpoint for identifying users upon login, and
 * generating a jwt token used to verify stateless http requests.
 */

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IntqtoolUserDetailService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;


    /**
     * This is the specific endpoint used to verify a users credentials, and generate a jwt token
     * the user can use in later requests.
     * Accessible on http://localhost:8080/authenticate.
     *
     * @param authenticationRequest a request object containing password and username from the request body.
     * @return jwt-token for the specific user.
     */
    @PostMapping(path = "/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
