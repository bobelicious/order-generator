package com.augusto.productsystemapi.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProductException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;
}