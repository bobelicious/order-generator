package com.augusto.productsystemapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.augusto.productsystemapi.config.FeignSupportConfig;
import com.augusto.productsystemapi.dtos.address.AddressViaCepDto;

@FeignClient(name = "viacep",url = "https://viacep.com.br/ws/" , configuration = FeignSupportConfig.class)
public interface ViaCepClient {
  @GetMapping(value = "/{cep}/json/")
  public AddressViaCepDto getAddress(@PathVariable()String cep);
}
