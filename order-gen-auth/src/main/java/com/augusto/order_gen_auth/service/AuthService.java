package com.augusto.order_gen_auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.augusto.order_gen_auth.dto.AccountCredentialsDto;
import com.augusto.order_gen_auth.dto.TokenDto;
import com.augusto.order_gen_auth.repository.UserRepository;
import com.augusto.order_gen_auth.security.JwtTokenProvider;
import com.augusto.order_gen_auth.exceptions.ResourceNotFoundException;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;

    public TokenDto signIn(AccountCredentialsDto credentials) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                credentials.username(),
                credentials.password())

        );
        var user = userRepository.findByEmailOrCpfOrUserNameAndDeletedFalse(credentials.username(),
                credentials.username(), credentials.username())
                .orElseThrow(() -> new ResourceNotFoundException("signin", "login", credentials.username()));
        var roles = user.getRoles().stream().map(role -> role.getRole()).toList();
        var token = tokenProvider.createAccessToken(credentials.username(), roles);

        return token;
    }

    public TokenDto refreshToken(String userName, String refreshToken) {
        userRepository.findByEmailOrCpfOrUserNameAndDeletedFalse(userName, userName, userName)
                .orElseThrow(() -> new ResourceNotFoundException("signin", "login", userName));
        var token = tokenProvider.refreshToken(refreshToken);
        return token;
    }
}
