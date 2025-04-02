package com.augusto.order_gen_auth.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AuthException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;
}