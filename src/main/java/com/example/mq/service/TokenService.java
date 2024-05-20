package com.example.mq.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.mq.model.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Service for token operations
 * Annotations:
 * - @Service: Indicates that an annotated class is a "Service"
 */
@Service
public class TokenService {
    @Value("${token.secret}")
    private String secret;

    /**
     * Method to generate token with sha256 algorithm
     * @param client Client object to generate token
     */
    public String generateToken(Client client) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("mq")
                    .withSubject(client.getEmail())
                    .withExpiresAt(getExpirationTime())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error creating token");
        }
    }

    /**
     * Method to validate token
     * @param token token to validate
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("mq")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }

    /**
     * Method to get expiration time of the token
     */
    private Instant getExpirationTime() {
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
    }
}
