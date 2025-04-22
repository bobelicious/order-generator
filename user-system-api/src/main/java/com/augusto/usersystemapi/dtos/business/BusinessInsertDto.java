package com.augusto.usersystemapi.dtos.business;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import com.augusto.usersystemapi.dtos.address.AddressInputDto;

import jakarta.validation.constraints.NotBlank;

public record BusinessInsertDto(
        @CNPJ(message = "Invalid CNPJ")
        String cnpj,
        @NotBlank(message = "Business name can't be empty")
        String businessName,
        @CPF(message = "Invalid CPF")
        String ownerCpf,
        AddressInputDto address,
        BigDecimal deliveryTax) {

}
