package com.augusto.productsystemapi.dtos.store;

import java.util.List;
import com.augusto.productsystemapi.dtos.address.AddressOutputDto;
import com.augusto.productsystemapi.dtos.product.ProductOutputDto;

public record StoreOutputDto(String cnpj, String name, List<ProductOutputDto> products, AddressOutputDto address ) {
  
}
