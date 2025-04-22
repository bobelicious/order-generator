package com.augusto.productsystemapi.dtos.product;

public record ProductInputDto(String name, int quantity, Float price, String cnpjOwner, String description) {

}
