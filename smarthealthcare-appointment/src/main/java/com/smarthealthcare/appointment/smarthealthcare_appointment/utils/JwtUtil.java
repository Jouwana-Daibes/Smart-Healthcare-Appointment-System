package com.smarthealthcare.appointment.smarthealthcare_appointment.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

/**
 * Utility class for generating and validating JWT tokens.
 * Handles JWT creation, validation, and parsing.
 */
@Component
public class JwtUtil {

    private final String secretKey = "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey";
    private final long secretKeyExpirationTimeMs = 3600000; // 1 hour

    // Generate JWT token
    public String generateToken(String username, Set<String> roles){
       return Jwts.builder()
               .setSubject(username)
               .claim("roles", roles)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + secretKeyExpirationTimeMs))
               .signWith(SignatureAlgorithm.HS256, secretKey)
               .compact();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Extract username from token
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
