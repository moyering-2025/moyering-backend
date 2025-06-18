package com.dev.moyering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class MoyeringApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoyeringApplication.class, args);
	}

}
