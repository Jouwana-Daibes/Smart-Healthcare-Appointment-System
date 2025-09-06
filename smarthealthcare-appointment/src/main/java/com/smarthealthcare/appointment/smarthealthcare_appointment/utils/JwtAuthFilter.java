package com.smarthealthcare.appointment.smarthealthcare_appointment.utils;

import com.smarthealthcare.appointment.smarthealthcare_appointment.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter that validates incoming HTTP requests.
 * This filter intercepts every HTTP request (once per request) and performs:
 *    - Extraction of the JWT token from the Authorization header
 *    - Validation of the token's signature and expiration
 *    - Loading the user details from the database using CustomUserDetailsService
 *    - Setting the authentication object in Spring Security's SecurityContext if the token is valid
 * Ensure only authenticated users with valid JWT tokens can access protected endpoints
 * Support role-based access control throughout the application
 */
 @Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

            final String reqAuthHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;

        if (reqAuthHeader == null || !reqAuthHeader.startsWith("Bearer")) {
                   filterChain.doFilter(request, response);
            return;
        }

        jwt = reqAuthHeader.substring(7); // remove the Bearer string rom the jwt token

        username = jwtUtil.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, username)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

               // authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Spring Security knows who the user is and their roles for authorization.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
