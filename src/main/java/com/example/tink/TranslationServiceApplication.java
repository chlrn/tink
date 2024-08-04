package com.example.tink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TranslationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TranslationServiceApplication.class, args);
	}

}

