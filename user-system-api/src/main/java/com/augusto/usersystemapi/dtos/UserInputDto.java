package com.augusto.usersystemapi.dtos;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserInputDto(
        String name,
        @Email(message = "Invalid email") String email,
        @CPF(message = "invalid CPF") String cpf, String phoneNumber,
        AddressInputDto addressInputDto,
        @NotEmpty(message = "field password not be null") 
        @Size(min = 6, message = "password need to be minimum 6 digits") 
        String password,
        @NotNull
        Long role,
        @NotEmpty
        String userName
        ) {
}