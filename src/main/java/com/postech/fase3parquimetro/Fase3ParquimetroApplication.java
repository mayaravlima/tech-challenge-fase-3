package com.postech.fase3parquimetro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Fase3ParquimetroApplication {

	public static void main(String[] args) {
		SpringApplication.run(Fase3ParquimetroApplication.class, args);
	}

}
