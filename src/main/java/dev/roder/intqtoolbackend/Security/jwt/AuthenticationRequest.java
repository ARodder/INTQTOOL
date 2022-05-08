package dev.roder.intqtoolbackend.Security.jwt;

/**
 * Class that represents the fields sent in a login request
 * containing the username and password.
 */
public class AuthenticationRequest {
    private String username;
    private String password;

    /**
     * Empty constructor required by jpa and spring
     */
    public AuthenticationRequest() {
    }

    /**
     * Constructor taking 2 parameters, username and password
     *
     * @param username username from the request
     * @param password password of the request
     */
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Retrieves the username from the authenticationRequest
     *
     * @return Returns username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username to a new value
     *
     * @param username new value for the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password from the authenticationRequest
     *
     * @return Returns the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the authenticationRequest to a new value
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
