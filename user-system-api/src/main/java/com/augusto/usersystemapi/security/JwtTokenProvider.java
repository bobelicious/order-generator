package com.augusto.usersystemapi.security;

import java.util.Base64;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.augusto.usersystemapi.exceptions.UserException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-lenght:3600000}") // 1h
    private long expirationInMillisecs;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public Authentication getAuth(String token) {
        var decodeJwt = decodedToken(token);
        var userDetails = this.customUserDetailsService.loadUserByUsername(decodeJwt.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        var alg = Algorithm.HMAC256(secretKey.getBytes());
        var verifier = JWT.require(alg).build();
        var decodedJwt = verifier.verify(token);
        return decodedJwt;
    }

    public String resolveToken(HttpServletRequest request) {
        var bearerToken = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    public Boolean validateToken(String token) {
        var decodedJwt = decodedToken(token);
        try {
            return decodedJwt.getExpiresAt().before(new Date()) ? false : true;

        } catch (Exception e) {
            throw new UserException(HttpStatus.UNAUTHORIZED, "Expired token");
        }
    }
}