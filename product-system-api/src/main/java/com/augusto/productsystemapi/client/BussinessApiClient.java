package com.augusto.productsystemapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.augusto.productsystemapi.config.FeignSupportConfig;

@FeignClient(name = "users-system-api", configuration = FeignSupportConfig.class)
public interface BussinessApiClient {
    @GetMapping("/list-business/{cnpj}")
    public String getBusinessByCnpj(@PathVariable String cnpj);
}
