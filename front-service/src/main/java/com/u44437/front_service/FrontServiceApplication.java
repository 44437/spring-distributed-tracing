package com.u44437.front_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FrontServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontServiceApplication.class, args);
	}

}
