package com.augusto.productsystemapi.dtos.address;

public record AddressViaCepDto(String cep, String logradouro, String bairro, String localidade,
    String uf) {

}
