package com.augusto.productsystemapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProductSystemApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductSystemApiApplication.class, args);
	}
}
