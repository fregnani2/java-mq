package com.example.mq.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.mq.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Value("${token.secret}")
    private String secret;

    /**
     * Method to generate token with sha256 algorithm
     * @param client Client object to generate token
     */
    public String generateToken(Client client) {
        try {
            logger.info("Generating token for client {}", client.getEmail());
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("mq")
                    .withSubject(client.getEmail())
                    .withExpiresAt(getExpirationTime())
                    .sign(algorithm);
            logger.info("Token generated");
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
            logger.info("Validating token");
            Algorithm algorithm = Algorithm.HMAC256(secret);
            logger.info("Token validated");
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
     * Method to get token expiration time
     */
    private Instant getExpirationTime() {
        logger.info("Getting token expiration time");
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
    }
}
