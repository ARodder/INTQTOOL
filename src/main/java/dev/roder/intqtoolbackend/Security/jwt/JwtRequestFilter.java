package dev.roder.intqtoolbackend.Security.jwt;

import dev.roder.intqtoolbackend.Security.IntqtoolUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Component filter used by spring to check for jwt token for
 * authentication in requests
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private IntqtoolUserDetailService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Retrieves jwt token from request if it exists,
     * and generates usernameAndPasswordAuthenticationToken to set securityContextHolder for continuing the request.
     *
     * @param request request sent by frontend
     * @param response response from the server
     * @param filterChain chain of filters used by spring
     * @throws ServletException Exception thrown by doFilter in filterChain
     * @throws IOException Exception thrown by doFilter in filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(upat);
            }
        }
        filterChain.doFilter(request, response);
    }
}
