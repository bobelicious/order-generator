package com.augusto.usersystemapi.dtos.business;

import java.math.BigDecimal;
import java.util.List;

import com.augusto.usersystemapi.dtos.address.AddressOutputDto;

public record BusinessOutPutDto(
        String businessName,
        String cnpj,
        List<Long> menu,
        AddressOutputDto address,
        String labelUrl,
        Double rating,
        boolean active,
        BigDecimal deliveryTax) {
}
