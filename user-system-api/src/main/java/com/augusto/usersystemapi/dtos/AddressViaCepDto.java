package com.augusto.usersystemapi.dtos;

public record AddressViaCepDto(String cep, String logradouro, String bairro, String localidade,
    String uf) {

}
