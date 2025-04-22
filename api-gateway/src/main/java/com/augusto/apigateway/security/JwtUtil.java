package com.augusto.apigateway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-lenght:3600000}") // 1h
    private long expirationInMillisecs;
    Algorithm algorithm = null;

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationInMillisecs))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String extractUsername(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            getDecodedJWT(token);
            return true;
        } catch (JWTVerificationException e) {
            logger.error("Erro ao validar token: {}", e.getMessage());
            return false;
        }
    }

    private DecodedJWT getDecodedJWT(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                .build();
        return verifier.verify(token);
    }
}