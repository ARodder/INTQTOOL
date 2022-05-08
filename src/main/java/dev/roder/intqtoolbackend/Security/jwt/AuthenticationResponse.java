package dev.roder.intqtoolbackend.Security.jwt;

/**
 * Class representing the response for an authentication request.
 * Contains generated JSONWebToken used for verification.
 */
public class AuthenticationResponse {
    private final String jwt;

    /**
     * Constructor containing 1 parameter the jwt token
     *
     * @param jwt value of the jwt token
     */
    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    /**
     * Retrieves the value of the jwt token
     *
     * @return Returns the jwt token
     */
    public String getJwt() {
        return jwt;
    }
}
