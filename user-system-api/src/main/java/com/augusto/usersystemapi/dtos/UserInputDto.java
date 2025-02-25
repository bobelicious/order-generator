package com.augusto.usersystemapi.dtos;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInputDto {
    private String name;
    @Email(message = "Invalid email")
    private String email;
    @CPF(message = "invalid CPF")
    private String cpf;
    private String phoneNumber;
    private AddressInputDto addressInputDto;
}