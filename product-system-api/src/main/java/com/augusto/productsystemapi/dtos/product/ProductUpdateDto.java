package com.augusto.productsystemapi.dtos.product;

public record ProductUpdateDto(
    String code,
    String name,
    int quantity,
    Float price
) {}
