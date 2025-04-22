package com.augusto.productsystemapi.dtos.product;

import java.util.List;

public record ProductOutputDto(
        String name,
        String addedDate,
        List<String> updatedDate,
        int quantity,
        String code,
        Float price,
        String cnpjOwner,
        String description
    ) {}
