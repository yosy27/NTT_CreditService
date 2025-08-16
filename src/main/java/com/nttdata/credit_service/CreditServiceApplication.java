package com.nttdata.credit_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// * Clase principal del microservicio Credit Service.
@SpringBootApplication(scanBasePackages = {"com.nttdata"})
public class CreditServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditServiceApplication.class, args);
	}

}
