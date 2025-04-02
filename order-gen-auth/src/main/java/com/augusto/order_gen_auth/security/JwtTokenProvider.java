package com.augusto.order_gen_auth.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.augusto.order_gen_auth.dto.TokenDto;
import com.augusto.order_gen_auth.exceptions.AuthException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
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

    public TokenDto createAccessToken(String username, List<String> roles) {
        var now = new Date();
        var validity = new Date(now.getTime() + expirationInMillisecs);
        var accessToken = getAccessToken(username, roles, now, validity);
        var refreshToken = getRefreshToken(username, roles, now);
        return new TokenDto(username, true, now, validity, accessToken, refreshToken);
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
        var issueUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create().withClaim("roles", roles).withIssuedAt(now).withExpiresAt(validity).withSubject(username)
                .withIssuer(issueUrl).sign(algorithm).toString();
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        var refreshValidity = new Date(now.getTime() + expirationInMillisecs);
        return JWT.create().withClaim("roles", roles).withIssuedAt(now).withExpiresAt(refreshValidity)
                .withSubject(username)
                .sign(algorithm).toString();
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
        if (StringUtils.isEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        } else {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "Invalid Token");
        }
    }

    public Boolean validateToken(String token) {
        var decodedJwt = decodedToken(token);
        try {
            return decodedJwt.getExpiresAt().before(new Date()) ? false : true;
            
        } catch (Exception e) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "Expired token");
        }
    }
}