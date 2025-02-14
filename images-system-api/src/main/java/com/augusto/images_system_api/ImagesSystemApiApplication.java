package com.augusto.images_system_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ImagesSystemApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImagesSystemApiApplication.class, args);
	}

}
