package com.augusto.productsystemapi.dtos.address;

public record AddressOutputDto(String cep, String street, String neighborhood, String city,
    String uf, String number) {

}
