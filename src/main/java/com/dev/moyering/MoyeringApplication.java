package com.dev.moyering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class MoyeringApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoyeringApplication.class, args);
	}

}
