package com.augusto.order_gen_auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.augusto.order_gen_auth.dto.AccountCredentialsDto;
import com.augusto.order_gen_auth.dto.TokenDto;
import com.augusto.order_gen_auth.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signIn(@RequestBody AccountCredentialsDto credentials) {
        return new ResponseEntity<TokenDto>(authService.signIn(credentials), HttpStatus.OK);

    }

    @PostMapping("/refresh/{username}")
    public ResponseEntity<TokenDto> signIn(@PathVariable String username,
            @RequestHeader("Authorization") String refreshToken) {
        return new ResponseEntity<TokenDto>(authService.refreshToken(username, refreshToken), HttpStatus.OK);

    }
}
