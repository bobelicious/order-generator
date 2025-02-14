package com.augusto.usersystemapi.dtos;



import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;

public record UserInputDto(String name, @Email(message = "Invalid email") String email,
    @CPF(message = "invalid CPF") String cpf, int phoneNumber, AddressInputDto addressInputDto) {
}