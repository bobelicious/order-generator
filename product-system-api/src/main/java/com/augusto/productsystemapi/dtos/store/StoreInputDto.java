package com.augusto.productsystemapi.dtos.store;

import com.augusto.productsystemapi.dtos.address.AddressInputDto;

public record StoreInputDto(String cnpj, String storeName, Long userOwner,
    AddressInputDto address) {

}
